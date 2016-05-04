package amazonHtml;

public class ProcessThread  implements Runnable{
	   private Thread t;
	   private Product product;
	   private String reviewPath;
	   public boolean running,success;//status and flag for the process
	   public ProcessThread( Product p,String rp){
	       reviewPath=new String(rp);
	       product=p;
	       running=true;
	       success=false;
	   }
	   public void run() {
		  success=false;
	     running=true;
	    // System.out.println("Running Thread" );
	     product.process(reviewPath);
	     if(product.savedReviews>0)
	    	 success=true;
	     running=false;
	    // System.out.println("Ending Thread" );
	   }
	   
	   public void start ()
	   {
		   running=true;
		   success=false;
	     // System.out.println("Starting Thread" );
	      if (t == null)
	      {
	         t = new Thread (this);
	         t.start ();
	      }
	   }

}
