from socket import *
serverName = 'localhost'
serverPort = 12000
clientSocket = socket(AF_INET,SOCK_STREAM) // 指定使用面向流的TCP协议
clientSocket.connect( (serverName,serverPort) )
sentence = input('Input lowercase sentence:')
clientSocket.send(sentence.encode())
modifiedSentence = clientSocket.recv(1024)
print('From Server:',modifiedSentence)
clientSocket.close()