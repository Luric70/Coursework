import socket
import os

def main():
    port = 8888
    serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM, proto=0)

    serverSocket.bind(('172.29.160.1', port))
    serverSocket.listen(3)
    print("Server is ready.")

    while True:
        connSocket, address = serverSocket.accept()
        sentence = connSocket.recv(1024).decode()
	
	    #parse file from http
        try:
            reqFile = sentence.split()[1]
        except IndexError:
            reqFile = ''

        print('Requested: ', reqFile)

	    #Getting HTTP Response
        path = os.getcwd() + '/' + reqFile
        if os.path.isfile(path):	#file found
            with open(path, 'rb') as file:
                data = file.read()

            media = extension(path)     #get content type
            #send http response of correct type
            response = f'HTTP/1.1 200 OK\r\nContent-Type: {media}\r\n\r\n'.encode() + data
        else:   #file not found
            response = 'HTTP/1.1 404 Not Found \r\n\r\n404 Not Found'.encode()
            
        connSocket.send(response)
        connSocket.close()
        
def extension(path):
    types = {
        '.html': 'text/html',
        '.txt': 'text/plain',
        '.jpg': 'image/jpeg',
        '.jpeg': 'image/jpeg',
        '.png': 'image/png',
    }
    ext = os.path.splitext(path)[1].lower()  #finds extension

    return types.get(ext, 'application/octet-stream')


if __name__ == '__main__':
    main()