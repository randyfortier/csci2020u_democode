#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <string.h>

void create_dns_query(char *dns_query);
void process_dns_response(char *dns_response);
short int get_short(char *data, int offset);
void get_ip_address(char *data, int offset, char *buffer);
int get_domain_name(char *data, int offset, char *buffer);
void copy_data(char *src, char *dst, int src_offset, int dst_offset, int num_bytes);
int cint(char cVal);
int process_answer(char *dns_response, int offset);

int main(int argc, char **argv) {

   if (argc <= 1) {
      printf("Usage:  dns_client <dnsserver>\n");
      exit(0);
   }

   // create a socket (unlike in Java, this only reserves the OS resources)
   int socketId = socket(PF_INET, SOCK_DGRAM, IPPROTO_UDP);
   if (socketId < 0) {
      printf("Error creating socket: %s\n", strerror(errno));
      exit(1);
   }

   // create a local address, so that the socket can receive the response
   struct sockaddr_in src_address;
   memset(&src_address, 0, sizeof(src_address));
   src_address.sin_family = AF_INET;
   src_address.sin_addr.s_addr = INADDR_ANY; // Socket layer fills in our local address
   src_address.sin_port = htons(12345);      // Local port: 12345

   // bind the socket to the local address
   //  Note: This is only necessary to receive datagrams, not sending
   if (bind(socketId,(struct sockaddr *)&src_address, sizeof(struct sockaddr)) < 0) {
      printf("Error binding socket: %s\n", strerror(errno));
      close(socketId);
      exit(1);
   }

   // create a destination address for the DNS server, based on the comment-line args
   struct sockaddr_in dest_address;
   memset(&dest_address, 0, sizeof(dest_address));
   dest_address.sin_family = AF_INET;
   // resolve the DNS server IP address
   int result = inet_pton(AF_INET, argv[1], &dest_address.sin_addr);
   if (result <= 0) {
      printf("Error resolving DNS server '%s'.\n", argv[1]);
      exit(1);
   }
   // You can also hard code addresses, like this:
   //   dest_address.sin_addr.s_addr = htonl(0xC0A80101); // 192.168.1.1
   dest_address.sin_port = htons(53);

   // create the buffer, which contains the DNS packet data
   char dns_query[31];
   create_dns_query(dns_query);

   // send the specified DNS packet data to the destination address
   int numBytes = sendto(socketId, dns_query, 31, 0, (struct sockaddr*)&dest_address, sizeof (struct sockaddr_in));
   if (numBytes < 0) {
      printf("Error sending packet: %s\n", strerror(errno));
   }

   // receive the response packet (this blocks until the packet is received)
   int recsize;
   int fromlen;
   char dns_response[200];
   recsize = recvfrom(socketId, (void *)dns_response, 200, 0, (struct sockaddr *)&src_address, &fromlen);

   // process the response
   process_dns_response(dns_response);

   // close the socket
   close(socketId);

   return 0;
}

void create_dns_query(char *dns_query) {
   // create the DNS header
   //   the DNS ID (we'll use 0x1234)
   dns_query[0] = 0x12;
   dns_query[1] = 0x34;
   //   the operation section
   //     0 - query flag
   //     0000 - query opcode
   //     0 - non-authoritative (only appropriate for responses)
   //     0 - not-truncated (only appropriate for responses)
   //     1 - recursion desired (so only a single query is needed)
   //  0x01
   dns_query[2] = 0x01;
   //     0 - recursion available (only appropriate for responses)
   //     000 - reserved (always zero)
   //     0000 - response code (only appropriate for responses)
   //  0x00
   dns_query[3] = 0x00;
   //   number of queries (1)
   dns_query[4] = 0x00;
   dns_query[5] = 0x01;
   //   number of answers (only appropriate for responses) (0)
   dns_query[6] = 0x00;
   dns_query[7] = 0x00;
   //   number of authoritative answers (only appropriate for responses) (0)
   dns_query[8] = 0x00;
   dns_query[9] = 0x00;
   //   number of additional answers (gratuitous) (only appropriate for responses) (0)
   dns_query[10] = 0x00;
   dns_query[11] = 0x00;

   // create the DNS question section
   //   size of www (3)
   dns_query[12] = 0x03;
   //   'www'
   dns_query[13] = 'w';
   dns_query[14] = 'w';
   dns_query[15] = 'w';
   //   size of google (6)
   dns_query[16] = 0x06;
   //   'google'
   dns_query[17] = 'g';
   dns_query[18] = 'o';
   dns_query[19] = 'o';
   dns_query[20] = 'g';
   dns_query[21] = 'l';
   dns_query[22] = 'e';
   //   size of ca (2)
   dns_query[23] = 0x02;
   //   'google'
   dns_query[24] = 'c';
   dns_query[25] = 'a';
   //   delimiter (0)
   dns_query[26] = 0x00;
   // record type (0x0001: A, which means 'Address')
   dns_query[27] = 0x00;
   dns_query[28] = 0x01;
   // address type (0x0001: IN, which means IPv4)
   dns_query[29] = 0x00;
   dns_query[30] = 0x01;
}

void process_dns_response(char *dns_response) {
   int num_answers = get_short(dns_response, 6);
   int num_authoritative = get_short(dns_response, 8);
   int num_additional = get_short(dns_response, 10);

   // skip to the answer section
   //  Note: This is a hack, since the we just happen to know the length of the query
   //        section.  Normally, you'd have to parse the question again and go from
   //        there.
   int offset = 31; // location of the first answer

   // process each answer that is returned
   int ans;
   for (ans = 0; ans < num_answers; ans++) {
      offset += process_answer(dns_response, offset);
   }

   // we'll ignore the authoritative name servers and additional sections
}

int process_answer(char *dns_response, int offset) {
   unsigned char data[200];

   int numBytes = 2; // skip initial 2 bytes

   int type = get_short(dns_response, offset + numBytes);
   numBytes += 2;    // count the type
   numBytes += 2;    // skip the address class (should be 0x0001, IPv4)
   numBytes += 4;    // skip the TTL (recommended cache time)

   int data_length1 = get_short(dns_response, offset + numBytes);
   numBytes += 2;

   if (type == 1) {
      // this is a normal query response (i.e. name -> address)
      get_ip_address(dns_response, offset + numBytes, data);
      numBytes += 4;
      printf("Response:  %u.%u.%u.%u\n", data[0],data[1],data[2],data[3]);
   } else if (type == 5) {
      // this is a CNAME query response (i.e. an alias)
      int data_length = get_domain_name(dns_response, offset + numBytes, data);
      numBytes += data_length;
      printf("Alias:  %s\n", data);
   }

   return numBytes;
}

// a convenience function to retrieve 2-byte integers from the byte array
short int get_short(char *data, int offset) {
   return ((short int)data[offset] * 256) + (short int)data[offset+1];
}

// a convenience function to retrieve a 4-byte IP address from the byte array
void get_ip_address(char *data, int offset, char *buffer) {
   buffer[0] = data[offset];
   buffer[1] = data[offset+1];
   buffer[2] = data[offset+2];
   buffer[3] = data[offset+3];
}

// a convenience function to retrieve a length-encoded domain name from the byte array
int get_domain_name(char *data, int offset, char *buffer) {
   int numBytes = 0;
   int numChars = 0;

   // determine how many characters are in the first block
   int block_size = (int)data[offset];
   numBytes += 1;
   while (block_size > 0) {
      // copy the correct number of bytes into the buffer
      copy_data(data, buffer, offset + numBytes, numChars, block_size);
      numBytes += block_size;
      numChars += block_size;

      // add the . character between blocks
      buffer[numChars] = '.';
      numChars++;

      // determine how many characters are in the next block
      block_size = (int)data[offset + numBytes];
      numBytes += 1;
   }

   if (block_size < 0)
      numBytes++;  // skip special characters that trail 'c0'

   numChars--; // get rid of the trailing '.', replacing it with a null-terminator
   buffer[numChars] = 0;

   return numBytes;
}

// a convenience function to copy data from one buffer to another
void copy_data(char *src, char *dst, int src_offset, int dst_offset, int num_bytes) {
   int i;
   for (i = 0; i < num_bytes; i++) {
      dst[dst_offset + i] = src[src_offset + i];
   }
}

