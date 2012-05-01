package examples.com.basho.riakcs.client;

public class Playground
{
	static final boolean runAgainstCS = true;
	static final boolean runAgainstS3 = false;
	static final boolean debugEnabled = true;
	static final boolean debugDisabled= false;


	public static void main(String[] args) throws Exception
	{
//		boolean debugFlag= debugDisabled;
		boolean debugFlag= debugEnabled;

//		UserOperations.runIt(debugFlag);

		BucketOperations.runIt(runAgainstCS, debugFlag);
		BucketOperations.runIt(runAgainstS3, debugFlag);

//		ObjectOperationsOnExistingBucket.runIt(runAgainstCS, debugFlag);
//		ObjectOperationsOnExistingBucket.runIt(runAgainstS3, debugFlag);

		StatisticOperations.runIt(debugFlag);

	}

}
