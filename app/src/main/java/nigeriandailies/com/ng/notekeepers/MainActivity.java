package nigeriandailies.com.ng.notekeepers;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NoteRecyclerAdapter mNoteRecyclerAdapter;
    DrawerLayout mDrawer;;
    private RecyclerView mRecyclerItems;
    private RecyclerView.LayoutManager mNoteLayoutManager;
    private CourseRecyclerAdapter mCourseRecyclerAdapter;
    private GridLayoutManager mCourseLayoutManager;
    private NoteKeepersOpenHelper mDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDbOpenHelper = new NoteKeepersOpenHelper(this);
        mDrawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NoteActivity.class));
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        initialiseDisplayContent();

    }

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        mAdapterNotes.notifyDataSetChanged();
        mNoteRecyclerAdapter.notifyDataSetChanged();
    }

    private void initialiseDisplayContent() {
        mRecyclerItems = findViewById(R.id.list_items);
        mNoteLayoutManager = new LinearLayoutManager(this);
        mCourseLayoutManager = new GridLayoutManager(this, 2);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        mNoteRecyclerAdapter = new NoteRecyclerAdapter(this, notes);
        mRecyclerItems.setAdapter(mNoteRecyclerAdapter);

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        mCourseRecyclerAdapter = new CourseRecyclerAdapter(this, courses);


        displayNote();
    }

    private void displayNote(){
        mRecyclerItems.setLayoutManager(mNoteLayoutManager);
        mRecyclerItems.setAdapter(mNoteRecyclerAdapter);

        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
        selectNavigationMenuItem(R.id.nav_notes);
    }
    private void selectNavigationMenuItem(int i){
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        menu.findItem(i).setChecked(true);
    }
    private void displayCourse(){
        mRecyclerItems.setLayoutManager(mCourseLayoutManager);
        mRecyclerItems.setAdapter(mCourseRecyclerAdapter);
        selectNavigationMenuItem(R.id.nav_courses);
    }

    @Override
    public void onBackPressed() {

        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_notes:
                displayNote();
                break;
            case R.id.nav_courses:
                displayCourse();
                break;
            case R.id.share:
                Toast.makeText(this, "Share message", Toast.LENGTH_LONG).show();
                break;
            case R.id.send:
                Toast.makeText(this, "Send", Toast.LENGTH_LONG).show();
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

}