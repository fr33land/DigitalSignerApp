package lt.freeland.DigitalSignerApp.Utils;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import lt.freeland.DigitalSignerApp.DialogWindows.PinInputDialog;

public class CustomCallbackHandler implements CallbackHandler {

    private long slot_;

    public CustomCallbackHandler(long slot_) {
        this.slot_ = slot_;
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof PasswordCallback) {
                PasswordCallback pwdCallback = (PasswordCallback) callbacks[i];
                PinInputDialog pinInput = new PinInputDialog(slot_);
                int res = pinInput.getStatus();

                if (res == PinInputDialog.PIN_OK) {
                    pwdCallback.setPassword(pinInput.getPin());
                } else {
                    pwdCallback.clearPassword();
                }
            } else {
                System.out.println(callbacks[i]);
            }
        }
    }

    public class CustomGiveUpException extends Exception {

        public CustomGiveUpException(String msg) {
            super(msg);
        }

        public CustomGiveUpException() {
            super();
        }
    }
}
