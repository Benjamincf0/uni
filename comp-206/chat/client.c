#include <stdio.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>

int main(int argc, char *argv[])
{
	struct sockaddr_in address;
	int sock_fd;
	char buf[1024];

	sock_fd = socket(AF_INET, SOCK_STREAM, 0);
	if (sock_fd < 0) {
		perror("socket");
		return 1;
	}

	address.sin_family = AF_INET;
	address.sin_addr.s_addr = htonl(INADDR_LOOPBACK); // 127.0.0.1 aka localhost
	address.sin_port = htons(7878);

	if (-1 == connect(sock_fd, (struct sockaddr *)&address, sizeof(address))) {
		perror("connect");
		return 1;
	}

	FILE *server = fdopen(sock_fd, "r+");

	while (NULL != fgets(buf, sizeof(buf), stdin)) {
		if (fprintf(server, "%s", buf) < 0) {
			perror("fprintf");
			fclose(server);
			return 1;
		}
		fflush(server);
	}

	fclose(server);
	return 0;
}
