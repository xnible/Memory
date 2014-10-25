package edu.ncc.memorygame;
//Elbin Martinez
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener{

	private ImageButton[] buttons;
	//private Button reset;
	private int numClicked;
	private int[] imageNums;
	private int[]buttonsClicked;
	private TextView welText;
	private boolean[] clicked;
	private String username;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Bundle b = this.getIntent().getExtras();
		username = b.getString("name");
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
		buttonsClicked[0] = 0;
		buttonsClicked[1] = 0;
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
			numClicked = 0;
			buttonsClicked[0] = 0;
			for (int i = 0; i < buttons.length; i++)
			{
				buttons[i].setImageResource(R.drawable.defaultsmile);
				buttons[i].setEnabled(true);
				clicked[i] = false;
			}
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
	public void onClick(final View view) 
	{
		int index = view.getId() - R.id.button0;
		buttons[index].setImageResource(imageNums[index]);
		clicked[index] = true;
		buttons[index].setEnabled(false);
		
		//I put the whole code into the Delay method because it would enable some of my buttons before the code finished
		new Handler().postDelayed(new Runnable()
		{
			public void run()

			{
				if(buttonsClicked[0] == 0)
					buttonsClicked[0] = view.getId();
				else
				{
					// delay setting the images back to the default so the user can see what was chosen

					// not a match, so set the image back to the default
					buttonsClicked[1] = view.getId();
					if(buttonsClicked[0] == buttonsClicked[1])
						return;
					if(imageNums[buttonsClicked[0] - R.id.button0] != imageNums[buttonsClicked[1] - R.id.button0])
					{

						buttons[buttonsClicked[0] - R.id.button0].setImageResource(R.drawable.defaultsmile);
						buttons[buttonsClicked[1] - R.id.button0].setImageResource(R.drawable.defaultsmile);
						buttons[buttonsClicked[0] - R.id.button0].setEnabled(true);
						buttons[buttonsClicked[1] - R.id.button0].setEnabled(true);
						clicked[buttonsClicked[0] - R.id.button0] = false;
						clicked[buttonsClicked[1] - R.id.button0] = false;
						buttonsClicked[0] = 0;
						buttonsClicked[1] = 0;
					}
					else
					{
						buttonsClicked[0] = 0;
						buttonsClicked[1] = 0;
					}
				}
				
				if(numClicked > 18)
				{
					buttons[view.getId() - R.id.button0].setImageResource(R.drawable.defaultsmile);
					Context context = getApplicationContext();
					int duration = Toast.LENGTH_LONG;

					Toast toast = Toast.makeText(context, "Sorry " + username + " you have passed 18 moves\nYou Lose!\n" , duration);
					toast.show();
					for (int i = 0; i < buttons.length; i++)
						buttons[i].setEnabled(false);
				}
				int count = 0;
				while((count < 12) && (clicked[count]==true))
				{

					if (count == 11)
					{
						Context context = getApplicationContext();
						int duration = Toast.LENGTH_LONG;

						Toast toast = Toast.makeText(context, "Congratulations!\nYou have found all the matches!", duration);
						toast.show();
					}
					count++;
				}

			}
		}, 1000);
		numClicked++;


	}

	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		int[] imgSet = new int[12];
		boolean[] current = new boolean[12];
		int numberOfTimeClicked = numClicked;
		int[] btnClicked = new int[2];

		for (int i = 0; i < buttonsClicked.length; i++)
			btnClicked[i] = buttonsClicked[i];

		for (int i = 0; i < imgSet.length; i++)
		{
			imgSet[i] = imageNums[i];
			current[i] = clicked[i];
		}

		savedInstanceState.putBooleanArray("Clicked", current);
		savedInstanceState.putIntArray("ImgSet", imgSet);
		savedInstanceState.putInt("NumClicked", numberOfTimeClicked);
		savedInstanceState.putIntArray("BtnClicked", btnClicked);
		super.onSaveInstanceState(savedInstanceState);
	}

	public void onRestoreInstanceState(Bundle restoreInstanceState)
	{
		int[] imgSet = new int[12];
		boolean[] current = new boolean[12];
		int numberOfTimeClicked;
		int[] btnClicked = new int[2];
		super.onRestoreInstanceState(restoreInstanceState);

		imgSet = restoreInstanceState.getIntArray("ImgSet");
		current = restoreInstanceState.getBooleanArray("Clicked");
		numberOfTimeClicked = restoreInstanceState.getInt("NumClicked");
		btnClicked = restoreInstanceState.getIntArray("BtnClicked");

		numClicked = numberOfTimeClicked;

		for (int i = 0; i < buttonsClicked.length; i++)
			buttonsClicked[i] = btnClicked[i];

		for (int i = 0; i < imgSet.length; i++)
		{
			imageNums[i] = imgSet[i];
			clicked[i] = current[i];
			buttons[i].setImageResource(R.drawable.defaultsmile);

			if (clicked[i] == true)
			{
				buttons[i].setImageResource(imageNums[i]);
				buttons[i].setEnabled(false);
			}
		}

		if (numClicked > 18)
			for (int i = 0; i < buttons.length; i++)
				buttons[i].setEnabled(false);
	}
}
