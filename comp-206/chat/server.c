#include <stdio.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>

int main(int argc, char* argv[])
{
	struct sockaddr_in address;
	int server_fd, client_fd;
	char buf[1024];

	server_fd = socket(AF_INET, SOCK_STREAM, 0);
	if (-1 == server_fd) {
		perror("socket");
		return 1;
	}

	address.sin_family = AF_INET;
	address.sin_addr.s_addr = htonl(INADDR_LOOPBACK); // 127.0.0.1 aka localhost
	// ^ this is a kind of security: the server will only listen
	// for incoming connections that come from 127.0.0.1 i.e. the same
	// computer that the server process is running on.
	address.sin_port = htons(7878);

	if (-1 == bind(server_fd, (struct sockaddr *)&address, sizeof(address))) {
		perror("bind");
		close(server_fd);
		return 1;
	}

	if (-1 == listen(server_fd, 5)) {
		perror("listen");
		close(server_fd);
		return 1;
	}

	while (1) {
		if (-1 == (client_fd = accept(server_fd, NULL, NULL))) {
			perror("accept");
			close(server_fd);
			return 1;
		}

		FILE *client = fdopen(client_fd, "r+");
		while (NULL != fgets(buf, sizeof(buf), client)) {
			printf("%s", buf);
		}
		fclose(client);
	}

	close(server_fd);
	return 0;
}
