import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;
interface ErlCallInterface {
    // Handle the current being issued
    Object command(OtpErlangAtom   command,
                   OtpErlangObject args);
    // Take the result from the command wrap it into something
    // manageable by a erlang node
    OtpErlangObject reply(Object reply);
}
