#pragma comment(lib, "ws2_32.lib")
#include <iostream>
#include <stdlib.h>
#include <winsock2.h>
#include <ws2tcpip.h>
#include <stdio.h>
#include<iostream>
using namespace std;
//#include <Ws2tcpip.h>
int main()
{
	WSADATA wsaData;
	int iResult;

	// Initialize Winsock
	iResult = WSAStartup(MAKEWORD(2, 2), &wsaData);
	if (iResult != 0) {
		printf("WSAStartup failed: %d\n", iResult);
		return 1;
	}

	std::cout << "server" << endl;;
	int s = socket(AF_INET, SOCK_DGRAM, 0);
	if (s == INVALID_SOCKET) {
		printf("sorry , failed\n");
		return 1;
	}
	sockaddr_in service;
	service.sin_family = AF_INET;
	service.sin_addr.s_addr = inet_addr("192.168.43.245");
	service.sin_port = htons(2000);

	int is_success_bind = bind(s, (struct sockaddr *)&service, sizeof(struct sockaddr));
	if (is_success_bind == SOCKET_ERROR) {
		printf("bind failed\n");
		closesocket(s);
		return 1;
	}
	if (is_success_bind == 0)
	{
		printf("successfully bind to the UDP port 2000");
	}
	int count = 0;
	int addr_len = sizeof(struct sockaddr_in);
	while (1)
	{
		char receved_data[25600];
		int len = recv(s, receved_data, sizeof(receved_data), 0);
		cout << count << endl;
	}
	closesocket(s);
}//


