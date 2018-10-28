#!/usr/bin/env python
# -*- coding=utf-8 -*-


"""
file: recv.py
socket service
"""


import socket
import threading
import time
import sys
import os
import struct


def socket_service():
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        s.bind(('172.18.34.215', 6666))
        s.listen(10)
    except socket.error as msg:
        print(msg)
        sys.exit(1)
    print ('The server is runnnig already,waiting for connection...')

    while 1:
        conn, addr = s.accept()
        #target 是被 run()方法调用的回调对象. 默认应为None, 意味着没有对象被调用。 
        #args是目标调用参数的tuple，默认为空元组()。 
        t = threading.Thread(target=deal_data, args=(conn, addr))
        t.start()

def deal_data(conn, addr):
    print ('Accept new connection from {0}'.format(addr) )
    #conn.settimeout(500)
    welcomeSentence  = 'Successful connection to the server!'
    conn.send(welcomeSentence.encode())

    while 1:

        filepath = conn.recv(1024).decode()
        if os.path.isfile(filepath):
            # 定义定义文件信息。128s表示文件名为128bytes长，l表示一个int或log文件类型，在此为文件大小
            fileinfo_size = struct.calcsize('128sl')
            # 定义文件头信息，包含文件名和文件大小
            # 定义文件头信息，包含文件名和文件大小
            #os.path.basename返回path最后的文件名
            #os.stat(filepath).st_size:普通文件以字节为单位的大小
            fhead = struct.pack('128sl', os.path.basename(filepath).encode(),
                                os.stat(filepath).st_size)
            conn.send(fhead)
            print ('File sending start...' )
            fp = open(filepath, 'rb')
            while 1:
                data = fp.read(1024)
                if not data:
                    print ('File sending over...')
                    print ('The Server is idle, waiting for connection...')
                    break
                conn.send(data)
        conn.close()
        break

        #计算占用的字节数
        fileinfo_size = struct.calcsize('128sl')
        buf = conn.recv(fileinfo_size)
        if buf:
            filename, filesize = struct.unpack('128sl', buf)
            #strip() 方法用于移除字符串头尾指定的字符（默认为空格或换行符）或字符序列。
            str = '\00'
            fn = filename.strip(str.encode())
            new_filename = os.path.join('./', 'new_' + fn.decode())
            print ('file new name is {0}, filesize if {1}'.format(new_filename,
                                                                 filesize) )

            recvd_size = 0  # 定义已接收文件的大小
            fp = open(new_filename, 'wb')
            print ('start receiving...')

            while not recvd_size == filesize:
                if filesize - recvd_size > 1024:
                    data = conn.recv(1024)
                    recvd_size += len(data)
                else:
                    data = conn.recv(filesize - recvd_size)
                    recvd_size = filesize
                fp.write(data)
            fp.close()
            print ('end receive...' )
        conn.close()
        break


if __name__ == '__main__':
    socket_service()
