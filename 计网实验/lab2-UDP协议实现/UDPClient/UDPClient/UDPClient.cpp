
#pragma comment(lib, "ws2_32.lib")
#include <iostream>
#include <winsock2.h>
#include <ws2tcpip.h>
#include <stdio.h>
using namespace std;
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

	//std::cout << "Hello World!\n";
	cout << "client" << endl;

	SOCKET s = socket(AF_INET, SOCK_DGRAM, 0);
	if (s == INVALID_SOCKET) {
		printf("sorry , failed\n");
		return 1;
	}

	sockaddr_in service;
	service.sin_family = AF_INET;
	service.sin_addr.s_addr = inet_addr("192.168.3.191");
	service.sin_port = htons(2000);

	for (int i = 0; i < 100; i++) {
		cout << i << endl;
		char hello[] = "Hello Vicky";
		int num = sendto(s, hello, sizeof(hello), 0, (SOCKADDR *)&service, sizeof(service));
		cout << "num: " << num << endl;
		//char check[] = " ";
		//Sleep(1000);
	}
	system("pause");
	closesocket(s);


}
