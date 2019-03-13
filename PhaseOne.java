import java.io.IOException;
import java.util.*;
        
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
        
public class PhaseOne 
{
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable>
    {
	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();
        
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
	{
	    /*String doc = value.toString();
	    String docPart[] = doc.split(" "); //spliting input string to get individual words
	    String docName = docPart[0]; //getting the document number or the document name
	    String tempStr=""; //temp string to construct the key part
	    //loop to collect all the words
	    //for loop counter i is starting as we have first element of each line as document number
	    
	    for(int i=1;i<docPart.length;i++)
	    {
		tempStr = docPart[i].replaceAll("\\p{P}", ""); //removing special character and punctuation from the word
		tempStr = tempStr+","+docName;
		word.set(tempStr);//converting string to text writable
		context.write(word,one);
	    }*/
		String docName = ((FileSplit) context.getInputSplit()).getPath().getName().toString();
		int count=0;
		String wordOne="";	
		String wordTwo="";
		StringTokenizer itr = new StringTokenizer(value.toString());
		while (itr.hasMoreTokens()) {
			if (count == 0) {
				wordOne = itr.nextToken().replaceAll("\\p{P}", "").trim();
				if (wordOne.length() < 1)
					continue;
				else
					count++;
			} else {
				wordOne = wordTwo;
			}
			wordTwo="";
			while (itr.hasMoreTokens()) {
				wordTwo = itr.nextToken().replaceAll("\\p{P}", "").trim();
				if (wordTwo.length() > 0)
					break;
			}
			word.set(wordOne + " " + wordTwo+","+docName);
			context.write(word, one);
		}

	}
    } 
        
    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable>
    {

	public void reduce(Text key, Iterable<IntWritable> values, Context context) 
	    throws IOException, InterruptedException
	    {
		int sum = 0;
		for (IntWritable val : values) 
		{
		    sum += val.get();
		}
		context.write(key, new IntWritable(sum));
	    }
    }
        
    public static void main(String[] args) throws Exception
    {
	Configuration conf = new Configuration();
        
        Job job = new Job(conf, "PhaseOne");
    
	job.setOutputKeyClass(Text.class);
	job.setOutputValueClass(IntWritable.class);
	job.setJarByClass(PhaseOne.class);

	job.setMapperClass(Map.class);
	job.setReducerClass(Reduce.class);
        
	job.setInputFormatClass(TextInputFormat.class);
	job.setOutputFormatClass(TextOutputFormat.class);
        
	FileInputFormat.addInputPath(job,new Path("/user/balasusp/q4input/"));
	FileOutputFormat.setOutputPath(job,new Path("/user/balasusp/q4/program3/phase1"));
        
	job.waitForCompletion(true);
    }
        
}

