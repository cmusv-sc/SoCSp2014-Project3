#! /usr/bin/env python
import time
import BaseHTTPServer
import cgi
import re
import os

HOST_NAME = 'localhost' # !!!REMEMBER TO CHANGE THIS!!!
PORT_NUMBER = 9000 # Maybe set this to 9000.

def _handle_search(s):
    print s.path
    m1 = re.search('SearchEngine/(.+?)/(.+?)/',s.path)
    m2 = re.search('SearchEngine/(.+?)/(.+)',s.path)
    m3 = re.search('SearchEngine/(.+?)/',s.path)
    m4 = re.search('SearchEngine/(.+)',s.path)
    name = ''
    if m1:
        name = re.sub('%20',' ',m1.groups()[0])
        query = re.sub('%20',' ',m1.groups()[1])
    elif m2:
        name = re.sub('%20',' ',m2.groups()[0])
        query = re.sub('%20',' ',m2.groups()[1])
    elif m3:
        query = re.sub('%20',' ',m3.groups()[0])
    else:
        query = re.sub('%20',' ',m4.groups()[0])

    output = os.popen("java SearchEngineCSV {0} {1}".format(name, query)).read()
    output = re.sub('\n','<br>',output)
    s.wfile.write(output)

class MyHandler(BaseHTTPServer.BaseHTTPRequestHandler):
    def do_HEAD(s):
        s.send_response(200)
        s.send_header("Content-type", "text/html")
        s.end_headers()

    def do_GET(s):
        """Respond to a GET request."""
        s.send_response(200)
        s.send_header("Content-type", "text/html")
        s.end_headers()
#        s.wfile.write("<html><head><title>Title goes here.</title></head>")
#        s.wfile.write("<body><p>This is a test.</p>")
        # If someone went to "http://something.somewhere.net/foo/bar/",
        # then s.path equals "/foo/bar/".
        if re.search("SearchEngine",s.path):
            _handle_search(s)
        else:
            s.wfile.write("<p>You accessed path: %s</p>" % s.path)
            s.wfile.write("</body></html>")

            
if __name__ == '__main__':
    server_class = BaseHTTPServer.HTTPServer
    httpd = server_class((HOST_NAME, PORT_NUMBER), MyHandler)
    print time.asctime(), "Server Starts - %s:%s" % (HOST_NAME, PORT_NUMBER)
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
    httpd.server_close()
    print time.asctime(), "Server Stops - %s:%s" % (HOST_NAME, PORT_NUMBER)
