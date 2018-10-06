#!/usr/bin/env python
# -*- coding=utf-8 -*-


"""
file: send.py
socket client
"""

import socket
import os
import sys
import struct


def socket_client():
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect(('127.0.0.1', 6666))
    except socket.error as msg:
        print (msg.decode())
        sys.exit(1)

    print (s.recv(1024).decode())
    while 1:
        filepath = input('Please enter the path to the file you requested: ')
        s.send(filepath.encode())

        #计算占用的字节数
        fileinfo_size = struct.calcsize('128sl')
        buf = s.recv(fileinfo_size)
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
                    data = s.recv(1024)
                    recvd_size += len(data)
                else:
                    data = s.recv(filesize - recvd_size)
                    recvd_size = filesize
                fp.write(data)
            fp.close()
            print ('end receive...' )
        s.close()
        break
        


if __name__ == '__main__':
    socket_client()

