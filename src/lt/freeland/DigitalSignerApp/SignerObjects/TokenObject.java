package lt.freeland.DigitalSignerApp.SignerObjects;

import java.security.Provider;

import lt.freeland.DigitalSignerApp.C_Functions;

public class TokenObject implements ISignerAppObject {

    private Object data;
    private long objectId;
    private C_Functions c_func;
    private Provider provider;

    public TokenObject(long objectId, Object data, C_Functions c_func, Provider pkcs11Provider) {
        this.data = data;
        this.objectId = objectId;
        this.setC_func(c_func);
        this.setProvider(pkcs11Provider);
    }

    @Override
    public void setObjectData(Object data) {
        this.data = data;
    }

    @Override
    public Object getObjectData() {
        return this.data;
    }

    @Override
    public String toString() {
        return data.toString();
    }

    @Override
    public void setObjectId(long id) {
        this.objectId = id;
    }

    @Override
    public long getObjectId() {
        // TODO Auto-generated method stub
        return this.objectId;
    }

    public C_Functions getC_func() {
        return c_func;
    }

    public void setC_func(C_Functions c_func) {
        this.c_func = c_func;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }
}
