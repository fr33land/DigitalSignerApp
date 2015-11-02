package lt.freeland.DigitalSignerApp.SignerObjects;

public class TokenDataObject implements ISignerAppObject
	{
		private Object data;
		private long objectId;
		
		public TokenDataObject(long objectId, Object data)
			{
				this.data = data;
				this.objectId = objectId;
			}

		@Override
		public void setObjectData(Object data)
			{
				this.data = data;				
			}

		@Override
		public Object getObjectData()
			{
				return this.data;
			}

		@Override
		public void setObjectId(long id)
			{
				this.objectId = id;				
			}

		@Override
		public long getObjectId()
			{
				return this.objectId;
			}
	}
