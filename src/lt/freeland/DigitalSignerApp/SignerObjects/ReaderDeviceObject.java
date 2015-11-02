package lt.freeland.DigitalSignerApp.SignerObjects;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ReaderDeviceObject implements ISignerAppObject
	{	
		private Object data;
		private long objectId;
		
		public ReaderDeviceObject(Object data)
			{
				this.data = data;
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
		public String toString() 
			{
			    return data.toString();
			}

		@Override
		public void setObjectId(long id)
			{
				this.objectId = id;				
			}

		@Override
		public long getObjectId()
			{
				// TODO Auto-generated method stub
				return this.objectId;
			}
	}
