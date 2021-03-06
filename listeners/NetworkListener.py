import SocketServer
from subprocess import*
import json
from threading import Timer

class NetworkListener(SocketServer.BaseRequestHandler):
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
            if "command" in dicts:

                command=dicts["command"]
                duration=dicts["duration"]

                proc=self.dumpLogcat(command,duration)

        except:
            print "parse failed"
            pass


    def terminate(self,process):
        if process.poll() is None:
            try:
                process.terminate()
            except:
                pass

    def dumpLogcat(self,command,duration):
            print command

            p = Popen(command, creationflags=CREATE_NEW_CONSOLE,stdout=PIPE, stderr=PIPE, bufsize=1, universal_newlines=True)
            print "here"
            timer = Timer(duration, self.terminate, args=[p])
            timer.start()

            p.wait()


            timer.cancel()
            '''
            while True:
                line = p.stdout.readline()
                flag=self.readFlag()
                print "found flag",flag
                if flag:
                    print "ready to kill"
                    p.kill()
                    break
                if not line:
                    break

                if line.strip()!="":
                    f.write(line)
                    f.flush()

            '''
            return p

    def dumpLogcatEvent(self):
        return



if __name__ == "__main__":
    HOST, PORT = "127.0.0.1", 10003 ## network

    # Create the server, binding to localhost on port 9999
    server = SocketServer.TCPServer((HOST, PORT), NetworkListener)
    # Activate the server; this will keep running until you
    # interrupt the program with Ctrl-C
    server.serve_forever()




