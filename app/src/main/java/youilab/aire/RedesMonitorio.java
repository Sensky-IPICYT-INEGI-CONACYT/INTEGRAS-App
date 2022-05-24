package youilab.aire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import youilab.aire.MVP.Interfaces.AllNetsList.AllNetsListPresenter;
import youilab.aire.MVP.Interfaces.AllNetsList.AllNetsListView;
import youilab.aire.MVP.Presenters.AllNetsListPresenterImpl;
import youilab.aire.adapters.MyAdapter;
import youilab.aire.IntegrASCore.Nets.Net;
import youilab.aire.processingdata.parserJSONAirAnalysisInformation;


public class RedesMonitorio extends AppCompatActivity implements AllNetsListView {
    Toolbar localToolbar;
    RelativeLayout laySearch;
    ImageView btnCloseSearch;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<Net> stationsNetList;


    private AllNetsListPresenter allNetsListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redes_monitorio);
        localToolbar = (Toolbar)findViewById(R.id.toolbarRedesMonitoreo);
        setSupportActionBar(localToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
      /*  localToolbar.setTitle("Fotografía");
*/
      laySearch = (RelativeLayout) findViewById(R.id.laySearch);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        btnCloseSearch = (ImageView) findViewById(R.id.closeSearch);
        btnCloseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i =laySearch.getRight();
                int j = laySearch.getTop() - (laySearch.getTop() - laySearch.getBottom()) / 2;
                int k = (int)Math.hypot(laySearch.getWidth(),laySearch.getHeight());
                Animator localAnimator1 = ViewAnimationUtils.createCircularReveal(laySearch, i, j, k, 0);
                localAnimator1.addListener(new Animator.AnimatorListener()
                {
                    public void onAnimationCancel(Animator paramAnonymousAnimator) {}

                    public void onAnimationEnd(Animator paramAnonymousAnimator)
                    {
                        RedesMonitorio.this.laySearch.setVisibility(View.GONE);
                        RedesMonitorio.this.localToolbar.setVisibility(View.VISIBLE);
                    }

                    public void onAnimationRepeat(Animator paramAnonymousAnimator) {}

                    public void onAnimationStart(Animator paramAnonymousAnimator) {}
                });
                localAnimator1.start();

                EditText txtSearch = (EditText) findViewById(R.id.txtSearch);
                txtSearch.setText("");
            }
        });
        EditText txtSearch = (EditText) findViewById(R.id.txtSearch);
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // specify an adapter (see also next example)
                if(stationsNetList.size()!=0) {
                    mAdapter = new MyAdapter(filterStationsList(stationsNetList, charSequence.toString()), getApplicationContext(),RedesMonitorio.this);
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        allNetsListPresenter = new AllNetsListPresenterImpl(this);
        allNetsListPresenter.getAllNets();
    }

    public List<Net> filterStationsList(List<Net> originalList, String stringToSearch)
    {
        List<Net> tmpList = new ArrayList<>();

        for (Net stationNet: originalList
             ) {
            if(stationNet.getStationList().get(0).getState().toLowerCase().indexOf(stringToSearch.toLowerCase())!=-1)
                tmpList.add(stationNet);

        }
        return tmpList;
    }
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nets, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            int m = localToolbar.getRight();
            int n = localToolbar.getTop() - (localToolbar.getTop() - localToolbar.getBottom()) / 2;
            int i1 = (int)Math.hypot(localToolbar.getWidth(), localToolbar.getHeight());
            Animator localAnimator2 = ViewAnimationUtils.createCircularReveal(laySearch, m, n, 0, i1);
            laySearch.setVisibility(View.VISIBLE);
            localAnimator2.start();
            localToolbar.setVisibility(View.GONE);
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void AllNets(List<Net> netList) {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        stationsNetList = netList;
        mAdapter = new MyAdapter(stationsNetList,getApplicationContext(), this);
        recyclerView.setAdapter(mAdapter);
    }
}
