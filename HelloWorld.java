import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;
class HelloWorld implements ErlCallInterface {
    public Object command(OtpErlangAtom    command,
                          OtpErlangObject  args) {
        System.out.println("Hello Erlang, you called me with the command: "
                           +command+" and these args: "+args+
                           ".\nI will something like this: {ok, \"Hello from Java\"");
        return "Hello from Java";
    }
    public OtpErlangObject reply(Object reply){
        OtpErlangObject otp_reply = (OtpErlangObject) reply;
        return otp_reply;
    }
}
