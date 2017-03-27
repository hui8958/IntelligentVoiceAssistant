package edu.hui.vassistant.activities;

import android.app.Activity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


import edu.hui.vassistant.R;

import edu.hui.vassistant.supports.MySQLiteOpenHelper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class DateMemoActivity extends Activity {
	public static final int INSERT = 0;	//�������
	public static final int UPDATE = 1;
	private MySQLiteOpenHelper helper;
	private Context c = this;
	private ListView listView;
	private Cursor cursor;
	private SimpleCursorAdapter adapter;
	
	private Dialog dialogMain;		//�鿴/�޸ĶԻ�����ӱ����Ի���
	private Dialog dialogQuery;		//�������ڲ�ѯ�Ի���
	
	private EditText editTextTitle;
	private EditText editTextContent;
	
	private Button btnSelectDate;
	private Button btnOK;
	private Button btnCancel;
	private Button btnDel;
	
	private Button btnQueryDate;
	private Button btnQueryOK;
	private Button btnQueryCancel;
	
	private TextView todayMemo;
	private int toDo;
	private int _id;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");     
	private String queryDate = dateFormat.format(new Date()); 
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mainevent);
        
        listView = (ListView)findViewById(R.id.ListView01);
        todayMemo = (TextView)findViewById(R.id.TextView02);
       
        helper = new MySQLiteOpenHelper(this);
        
        /* ��ѯ����ı��� */
        cursor = helper.select(queryDate);
        
        
        adapter = new SimpleCursorAdapter(
        		this,
        		R.layout.list_view,
        		cursor,
        		new String[]{MySQLiteOpenHelper.titleField,MySQLiteOpenHelper.contentField},
        		new int[]{R.id.text1,R.id.text2}
        );
        
        listView.setAdapter(adapter);
        
        refreshData();
  
        dialogMain = new Dialog(this);
        LayoutInflater inflater1 = LayoutInflater.from(this);
        final View dialogView = inflater1.inflate(R.layout.maindialog_view, null);
        dialogMain.setContentView(dialogView);
  
        dialogQuery = new Dialog(this);
        LayoutInflater inflater2 = LayoutInflater.from(this);
        final View queryView = inflater2.inflate(R.layout.querydialog_view, null);
        dialogQuery.setContentView(queryView);
        
        editTextTitle = (EditText)dialogView.findViewById(R.id.EditText01);
        editTextContent = (EditText)dialogView.findViewById(R.id.EditText02);
        btnSelectDate = (Button)dialogView.findViewById(R.id.Button03);
        btnOK = (Button)dialogView.findViewById(R.id.Button01);
        btnCancel = (Button)dialogView.findViewById(R.id.Button02);
        btnDel = (Button)dialogView.findViewById(R.id.Button04);
        
        btnQueryDate = (Button)queryView.findViewById(R.id.Buttonselectdate);
        btnQueryOK = (Button)queryView.findViewById(R.id.Buttonqueryok);
        btnQueryCancel = (Button)queryView.findViewById(R.id.Buttonqueryback);
        
        /*���鿴/�޸ĶԻ�����ӱ����Ի���ѡ�����ڰ�ť����Ӧ
         * ���Button��TextΪѡ���������ʾ����������ӡ��޸�getText����*/
        btnSelectDate.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				int year = 0;
				int month = 0;
				int day = 0;
				if ( toDo == INSERT ){
					Calendar calendar = new GregorianCalendar();
					
					year = calendar.get(Calendar.YEAR);
					month = calendar.get(Calendar.MONTH);
					day = calendar.get(Calendar.DAY_OF_MONTH);
				}else if ( toDo == UPDATE ){
					String dt = btnSelectDate.getText().toString();
					Date d = null;
					Calendar c = null;
					try {
						d = new SimpleDateFormat("yyyy-MM-dd").parse(dt);
						c = new GregorianCalendar();
						c.setTime(d);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					year = c.get(Calendar.YEAR);
					month = c.get(Calendar.MONTH);
					day = c.get(Calendar.DAY_OF_MONTH);
				}
				
				
				DatePickerDialog dpd = new DatePickerDialog(
						dialogView.getContext(),
						new OnDateSetListener(){
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								Calendar calendar = new GregorianCalendar(
										year, monthOfYear, dayOfMonth
								);
								btnSelectDate.setText(
										new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime())
								);
							}							
						},
						year,
						month,
						day);
				dpd.show();
			}        	
        });
        
        /*(�鿴/�޸ĶԻ�����ӱ����Ի���)ȷ����ť����Ӧ
         * �������ݿ���ʣ���ӻ��޸ı�����ˢ������*/
        btnOK.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				if(btnSelectDate.getText().toString() == c.getString(R.string.selectdate)){
					Toast.makeText(c, "DATE CANNOT BE NULL", Toast.LENGTH_SHORT).show();
				}
				else{
					switch (toDo) {
					case INSERT:
						helper.insert(
								editTextTitle.getText().toString(), 
								editTextContent.getText().toString(), 
								btnSelectDate.getText().toString());
						break;
					case UPDATE:
						helper.update(
								_id+"",
								editTextTitle.getText().toString(), 
								editTextContent.getText().toString(), 
								btnSelectDate.getText().toString());
					}
					
					refreshData();
					dialogMain.cancel();
				}
			}        	
        });
        
        /*(�鿴/�޸ĶԻ���)ɾ��������ť����Ӧ*/
        btnDel.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v) {
			Builder builder = new Builder(DateMemoActivity.this);
			builder.setTitle(R.string.deletememo);
			builder.setMessage("MAKE SURE YOU WANT TO DELETE THIS ITEM?");
			builder.setPositiveButton(R.string.ok, new OnClickListener(){

				public void onClick(DialogInterface dialog,
						int which) {
					helper.delete(cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.idField))+"");
					refreshData();
				}
				
			});
			builder.setNegativeButton(R.string.cancel, new OnClickListener(){

				public void onClick(DialogInterface dialog,
						int which) {
					
				}
				
			}).show();		
			dialogMain.cancel();
		}        	
    });
        
        /*(�鿴/�޸ĶԻ�����ӱ����Ի���)ȡ����ť����Ӧ*/
        btnCancel.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				dialogMain.cancel();
			}        	
        });
        
        /*(��ѯ�����Ի���)ѡ�����ڰ�ť����Ӧ
         * ���Button��TextΪѡ���������ʾ�������ڲ�ѯʱgetText����*/
        btnQueryDate.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				int year = 0;
				int month = 0;
				int day = 0;
				
				Calendar calendar = new GregorianCalendar();
					
				year = calendar.get(Calendar.YEAR);
				month = calendar.get(Calendar.MONTH);
				day = calendar.get(Calendar.DAY_OF_MONTH);
				
				
				DatePickerDialog dpd = new DatePickerDialog(
						dialogView.getContext(),
						new OnDateSetListener(){
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								Calendar calendar = new GregorianCalendar(
										year, monthOfYear, dayOfMonth
								);
								btnQueryDate.setText(
										new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime())
								);
							}							
						},
						year,
						month,
						day);
				dpd.show();
			}        	
        });
        
        /*(��ѯ�����Ի���)ȷ�ϰ�ť����Ӧ
         * �������ݿ�ķ��ʣ�ˢ������*/
        btnQueryOK.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				if(btnQueryDate.getText().toString() == c.getString(R.string.selectdate)){
					Toast.makeText(c, "DATE CAN NOT BE NULL", Toast.LENGTH_SHORT).show();
				}
				else{
					queryDate = btnQueryDate.getText().toString();
					cursor = helper.select(queryDate);
					
					adapter = new SimpleCursorAdapter(
			        		c,
			        		R.layout.list_view,
			        		cursor,
			        		new String[]{MySQLiteOpenHelper.titleField,MySQLiteOpenHelper.contentField},
			        		new int[]{R.id.text1,R.id.text2}
			        );
			        
			        listView.setAdapter(adapter);
					refreshData();
					dialogQuery.cancel();
				}  
			}
        });
        
        /*(��ѯ�����Ի���)ȡ����ť����Ӧ*/
        btnQueryCancel.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				dialogQuery.cancel();
			}        	
        });
        
        /*���listView�б���ʱ�������鿴/�޸ĶԻ���*/
        listView.setOnItemClickListener(new ListView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				cursor.moveToPosition(position);
				btnDel.setVisibility(View.VISIBLE);
				btnOK.setText(R.string.updatememo);
				editTextTitle.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.titleField)));
				editTextContent.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.contentField)));
				btnSelectDate.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.dateField)));
				
				toDo = UPDATE;
				_id = cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.idField));
				DateMemoActivity.this.dialogMain.setTitle(R.string.check_update);
				DateMemoActivity.this.dialogMain.show();
			}        	
        });
        
        
        Intent intent = getIntent();
        if(intent.getStringExtra("command").equals("add")){
        	toDo = INSERT;
			btnDel.setVisibility(View.GONE);
			btnOK.setText(R.string.ok);
			dialogMain.setTitle(R.string.add);
			
			resetDialogData();
			dialogMain.show();
        	String title = intent.getStringExtra("title");
        	String content =  intent.getStringExtra("content");
        //	EditText tet = (EditText) findViewById(R.id.EditText01);
        	//EditText cet = (EditText) findViewById(R.id.EditText02);
        	editTextTitle.setText(title);
        	editTextContent.setText(content);
        }else if(intent.getStringExtra("command").equals("viewOne")){
        	dialogQuery.setTitle(R.string.select);
			dialogQuery.show();
        }else if(intent.getStringExtra("command").equals("viewAll")){
        	cursor = helper.select();		
			adapter = new SimpleCursorAdapter(
	        		c,
	        		R.layout.list_view,
	        		cursor,
	        		new String[]{MySQLiteOpenHelper.titleField,MySQLiteOpenHelper.contentField},
	        		new int[]{R.id.text1,R.id.text2}
	        );
	        
	        listView.setAdapter(adapter);
	        queryDate = "All";
	        refreshData();
        }else if(intent.getStringExtra("command").equals("deleteAll")){
        	Builder builder = new Builder(DateMemoActivity.this);
			builder.setTitle(R.string.deletememo);
			builder.setMessage("MAKE SURE YOU WANT TO DELETE ALL ITEMS");
			builder.setPositiveButton(R.string.ok, new OnClickListener(){

				public void onClick(DialogInterface dialog,
						int which) {
					helper.delete();
					refreshData();
				}
				
			});
			builder.setNegativeButton(R.string.cancel, new OnClickListener(){

				public void onClick(DialogInterface dialog,
						int which) {
					
				}
				
			}).show();
        }
        
    }
    public void Onfinish(){
    	cursor.close();
    }
    
    /*����menu��ѡ��*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(1, 1, 1, R.string.add);		//��ӱ���
    	menu.add(1, 2, 2, R.string.select);		//��ѯĳ�յı���
    	menu.add(1, 3, 3, R.string.findall);	//��ѯ���еı���
    	menu.add(1, 4, 4, R.string.delall);		//ɾ�����еı���
    	
    	return super.onCreateOptionsMenu(menu);
    }
    
    /*Ϊmenuѡ��������Ӧ*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {
		case 1:
			toDo = INSERT;
			btnDel.setVisibility(View.GONE);
			btnOK.setText(R.string.ok);
			dialogMain.setTitle(R.string.add);
			resetDialogData();
			dialogMain.show();
			break;
		case 2:
			dialogQuery.setTitle(R.string.select);
			dialogQuery.show();
			break;
		case 3:
			cursor = helper.select();
			
			adapter = new SimpleCursorAdapter(
	        		c,
	        		R.layout.list_view,
	        		cursor,
	        		new String[]{MySQLiteOpenHelper.titleField,MySQLiteOpenHelper.contentField},
	        		new int[]{R.id.text1,R.id.text2}
	        );
	        
	        listView.setAdapter(adapter);
	        queryDate = "All";
	        refreshData();
			break;
		case 4:
			Builder builder = new Builder(DateMemoActivity.this);
			builder.setTitle(R.string.deletememo);
			builder.setMessage("MAKE SURE YOU WANT TO DELETE ALL ITEMS");
			builder.setPositiveButton(R.string.ok, new OnClickListener(){

				public void onClick(DialogInterface dialog,
						int which) {
					helper.delete();
					refreshData();
				}
				
			});
			builder.setNegativeButton(R.string.cancel, new OnClickListener(){

				public void onClick(DialogInterface dialog,
						int which) {
					
				}
				
			}).show();
			//refreshData();
			break;
		}
    	
    	return super.onOptionsItemSelected(item);
    }
    
    /**���ò鿴/�޸ĶԻ�����ӱ����Ի������ʾ**/
    private void resetDialogData(){
    	editTextTitle.setText("");
    	editTextContent.setText("");
    	btnSelectDate.setText(R.string.selectdate);
    }
    
    /**ˢ��listView�е�����**/
    private void refreshData(){
    	cursor.requery();
    	if ( cursor.getCount() == 0 ){
        	todayMemo.setText(queryDate+"'s events\nNo events exists");
        }else{
        	todayMemo.setText(queryDate+"'s events");
        }
		listView.invalidateViews();
    }

}