import com.ericsson.otp.erlang.*;
// Some class

public class ErlCallNode {
    /**
     * Input params:
     * arg[0] - node name
     * arg[1] - mailbox name
     * arg[2] - cookie name

     Example of launcher:
     ServiceWrapper foo@server service erlang

     Remember that the cookie needs to be shared between the nodes, example:
     erl -sname bar@server -setcookie cookie_name
     or
     erlang:set_cookie('cookie_name'). within a shell

     This should now be possible if everything is well:
     $> net:ping('foo@server')
     pong

     Send a message to this server:
     {'mailbox_name', 'foo@server'} ! {self(), Command, Args}
     */
    public static void main(String[] args) throws Exception {
        String  node_name = args[0];
        String  mbox_name = args[1];
        String  cookie    = args[2];
        OtpNode node      = new OtpNode(node_name, cookie);
        OtpMbox mbox      = node.createMbox(mbox_name);
        System.out.println("ServiceWrapper launched "+
                           "Node name: "+node_name+" "+
                           "mbox name: "+mbox_name+" "+
                           "Cookie/secret: "+cookie);
        // Keep on looping and receive messages
        while(true) {
            // Expects a tuple {pid, command, params}
            OtpErlangTuple  message = (OtpErlangTuple) mbox.receive();
            OtpErlangPid    pid     = (OtpErlangPid) message.elementAt(0);
            OtpErlangAtom   command = (OtpErlangAtom) message.elementAt(1);
            OtpErlangObject params  = (OtpErlangObject) message.elementAt(2);
            if (command.atomValue().equals("stop")) {
                break;
            } else {
                ErlCallInterface aErlCallInstance = new HelloWorld();
                handle_message(mbox, pid, command, params, aErlCallInstance);
            }
        }
        // Tear down node
        node.closeMbox(mbox);
        node.close();
    }

    private static void handle_message(OtpMbox          mbox,
                                       OtpErlangPid     pid,
                                       OtpErlangAtom    command,
                                       OtpErlangObject  args,
                                       ErlCallInterface callee) {
        // Do a bunch of fancy stuff with the command and jada jada...
        System.out.println("Received this command: "+ command +
                           " and these args: "+args+
                           " will respond");
        // Failure here should create a error result towards the
        // erlang node
        Object java_result            = callee.command(command, args);
        OtpErlangObject otp_reply     = callee.reply(java_result);
        mbox.send(pid, otp_reply);
    }

    // A error should return {error, Yada} instead...
    private static OtpErlangTuple wrap_result(OtpErlangObject result) {
        OtpErlangObject[] reply = new OtpErlangObject[2];
        reply[0]                = new OtpErlangAtom("ok");
        reply[1]                = result;
        return new OtpErlangTuple(reply);
    }
}
