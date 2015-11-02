package lt.freeland.DigitalSignerApp.SignerObjects;

public interface ISignerAppObject
	{
		public void setObjectData(Object data);
		public Object getObjectData();
		public void setObjectId(long id);
		public long getObjectId();
	}
