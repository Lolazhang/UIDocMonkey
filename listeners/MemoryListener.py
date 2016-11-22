import SocketServer
from subprocess import*
import json
from threading import Timer

from datetime import datetime
import time
class MemoryListener(SocketServer.BaseRequestHandler):
    """
    The RequestHandler class for our server.

    It is instantiated once per connection to the server, and must
    override the handle() method to implement communication to the
    client.
    """

    def handle(self):
        # self.request is the TCP socket connected to the client
        self.data = self.request.recv(10240)
        self.data = self.data.strip()
        print self.data
        self.procs=[]
        self.HandleMessage(self.data)

    def HandleMessage(self,data):
        try:
            dicts=json.loads(data.strip())
            print dicts
            if "tag" in dicts:
                tag=dicts["tag"]
                if tag=="start":

                    command=dicts["command"]
                    filename=dicts["filename"]
                    duration=dicts["duration"]
                    sample=dicts["sample"]

                    self.Monitor(filename,command,duration,sample)


        except:
            print "parse failed"
            pass


    def terminate(self,process):
        if process.poll() is None:
            try:
                process.terminate()
            except:
                pass

    def Monitor(self,filename,command,duration,sample):
            print command
            elapsed=0
            current=self.getCurrentTime()
            fw=file(filename,'w')
            while elapsed<duration*1000.0:
                contents = "timestamp:"+str(self.getCurrentTime())+"\n"
                p = Popen(command, stdout=PIPE)
                contents+=p.stdout.read()
                #print "contents",contents
                fw.write(contents+"\n")
                fw.flush()

                time.sleep(sample/1000.0)

                elapsed=self.getCurrentTime()-current
            #MemoryDump(contents,jsonwriter)





    def getCurrentTime(self):
        current = datetime.now()
        d = current - datetime(1970, 1, 1)
        dt = int(d.total_seconds() * 1000)
        return dt




if __name__ == "__main__":
    HOST, PORT = "127.0.0.1", 10006

    # Create the server, binding to localhost on port 9999
    server = SocketServer.TCPServer((HOST, PORT), MemoryListener)
    # Activate the server; this will keep running until you
    # interrupt the program with Ctrl-C
    server.serve_forever()




