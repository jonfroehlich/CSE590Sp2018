package makeabilitylab.realtimegraphviewdemo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

/**
 * Shows two basic instances of a real-time graph in GraphView: http://www.android-graphview.org/
 * ;
 *
 *
 * @author jonf
 */
public class MainActivity extends AppCompatActivity {

    // We use timers to intermittently generate random data for the two graphs
    private final Handler _handler = new Handler();
    private Runnable _timer1;
    private Runnable _timer2;

    private LineGraphSeries<DataPoint> _series1;
    private LineGraphSeries<DataPoint> _series2;
    private double _graph2LastXValue = 30d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GraphView graph = (GraphView) this.findViewById(R.id.graph);
        _series1 = new LineGraphSeries<>(generateData());
        graph.addSeries(_series1);
        graph.setTitle("Real-Time Graph (Non-Scrolling)");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Random Data");

        GraphView graph2 = (GraphView) this.findViewById(R.id.graph2);
        _series2 = new LineGraphSeries<>(generateData());
        graph2.setTitle("Real-Time Graph (Scrolling)");
        graph2.addSeries(_series2);
        graph2.getGridLabelRenderer().setVerticalAxisTitle("Random Data");
        graph2.getViewport().setXAxisBoundsManual(true);
        graph2.getViewport().setMinX(0);
        graph2.getViewport().setMaxX(40);
    }

    @Override
    public void onResume() {
        super.onResume();
        _timer1 = new Runnable() {
            @Override
            public void run() {
                _series1.resetData(generateData());
                _handler.postDelayed(this, 300);
            }
        };
        _handler.postDelayed(_timer1, 300);

        _timer2 = new Runnable() {
            @Override
            public void run() {
                _graph2LastXValue += 1d;
                _series2.appendData(new DataPoint(_graph2LastXValue, getRandom()), true, 40);
                _handler.postDelayed(this, 200);
            }
        };
        _handler.postDelayed(_timer2, 1000);
    }

    @Override
    public void onPause() {
        _handler.removeCallbacks(_timer1);
        _handler.removeCallbacks(_timer2);
        super.onPause();
    }

    /**
     * Helper function to generate data for the graph
     * @return
     */
    private DataPoint[] generateData() {
        int count = 30;
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i;
            double f = _rand.nextDouble() * 0.15 + 0.3;
            double y = Math.sin(i*f+2) + _rand.nextDouble()*0.3 + 5;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

    double _lastRandom = 2;
    Random _rand = new Random();
    private double getRandom() {
        return _lastRandom += _rand.nextDouble() * 0.5 - 0.25;
    }
}
