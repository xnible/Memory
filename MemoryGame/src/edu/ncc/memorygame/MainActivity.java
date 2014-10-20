package edu.ncc.memorygame;
//Elbin
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener{

	private ImageButton[] buttons;
	//private Button reset;
	private int numClicked;
	private int[] imageNums;
	private int[]buttonsClicked;
	private TextView welText;
	private boolean[] clicked; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Bundle b = this.getIntent().getExtras();
		String username = b.getString("name");
		welText = (TextView)findViewById(R.id.welcome_text);
		welText.setText("Welcome " + username);
		// create the array to store references to the buttons
		buttons = new ImageButton[12];
		clicked = new boolean[12];
		

		// get the id of the first button
		int idIndex = R.id.button0;

		// store the references into the array by cycling through the id indices & set the listener
		for (int i=0; i<buttons.length; i++) 
		{
			buttons[i] = (ImageButton)findViewById(idIndex++);
			buttons[i].setOnClickListener(this);
			clicked[i] = false;
		}

		//get the id's for the images and store 2 of each
		imageNums = new int[12];
		imageNums[0] = R.drawable.angry;
		imageNums[1] = R.drawable.angry;
		imageNums[2] = R.drawable.blushing;
		imageNums[3] = R.drawable.blushing;
		imageNums[4] = R.drawable.crying;
		imageNums[5] = R.drawable.crying;
		imageNums[6] = R.drawable.haha;
		imageNums[7] = R.drawable.haha;
		imageNums[8] = R.drawable.sad;
		imageNums[9] = R.drawable.sad;
		imageNums[10] = R.drawable.smile;
		imageNums[11] = R.drawable.smile;

		// randomize the values
		int r1, r2;
		int temp;
		for (int i=0; i<20; i++)
		{
			r1 = (int)(Math.random()*12);
			r2 = (int)(Math.random()*12);
			temp = imageNums[r1];
			imageNums[r1] = imageNums[r2];
			imageNums[r2]= temp;
		}

		//reset = (Button)findViewById(R.id.reset_button);
		//reset.setOnClickListener(this);

		
		numClicked = 0;
		buttonsClicked = new int[2];
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) 
		{
			return true;
		}
		else if(id == R.id.action_reset) 
		{
			for (int i = 0; i < buttons.length; i++)
				buttons[i].setImageResource(R.drawable.defaultsmile);
			int r1, r2;
			int temp;
			for (int i=0; i<20; i++)
			{
				r1 = (int)(Math.random()*12);
				r2 = (int)(Math.random()*12);
				temp = imageNums[r1];
				imageNums[r1] = imageNums[r2];
				imageNums[r2]= temp;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view) 
	{
		int index = view.getId() - R.id.button0;
		buttons[index].setImageResource(imageNums[index]);
		clicked[index] = true;
	}

	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		int[] imgSet = new int[12];
		boolean[] current = new boolean[12];
		for (int i = 0; i < imgSet.length; i++)
		{
			imgSet[i] = imageNums[i];
			current[i] = clicked[i];
		}

		savedInstanceState.putBooleanArray("clicked", current);
		savedInstanceState.putIntArray("imgSet", imgSet);
		super.onSaveInstanceState(savedInstanceState);
	}

	public void onRestoreInstanceState(Bundle restoreInstanceState)
	{
		int[] imgSet = new int[12];
		boolean[] current = new boolean[12];
		super.onRestoreInstanceState(restoreInstanceState);

		imgSet = restoreInstanceState.getIntArray("imgSet");
		current = restoreInstanceState.getBooleanArray("clicked");

		for (int i = 0; i < imgSet.length; i++)
		{
			imageNums[i] = imgSet[i];
			clicked[i] = current[i];
			
			if (clicked[i] == true)
				buttons[i].setImageResource(imageNums[i]);
		}
	}
}
