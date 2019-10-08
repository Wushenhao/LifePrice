package com.example.casper.lifeprice;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LifePriceMainActivity extends AppCompatActivity {


    private ArrayList<Good> theGoods;
    private ListView listViewSuper;
    private GoodsArrayAdapter theAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_price_main);

        InitData();

        listViewSuper= (ListView) this.findViewById(R.id.list_view_goods);
        theAdapter=new GoodsArrayAdapter(this,R.layout.list_item_good,theGoods);
        listViewSuper.setAdapter(theAdapter);
        this.registerForContextMenu(listViewSuper); //为控件注册场景菜单
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v==listViewSuper) {
            int itemPosition=((AdapterView.AdapterContextMenuInfo)menuInfo).position;
            menu.setHeaderTitle(theGoods.get(itemPosition).getName());  //前两行在点击时判断位置
            menu.add(0, 1, 0, "新建");
            menu.add(0, 2, 0, "删除");
            menu.add(0, 3, 0, "关于...");
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case 1: {
                AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

                theGoods.add(menuInfo.position+1, new Good("new fish",4, R.drawable.a4));
                theAdapter.notifyDataSetChanged(); //通知adapter底层数据已改变，修改数据
                Toast.makeText(this, "你选择了新建", Toast.LENGTH_SHORT).show();

                break;
            }
            case 2: {
                AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                final int itemPosition=menuInfo.position;

                new android.app.AlertDialog.Builder(this)
                        .setTitle("询问")
                        .setMessage("你确定要删除这条吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                theGoods.remove(itemPosition);
                                theAdapter.notifyDataSetChanged(); //通知adapter底层数据已改变，修改数据
                                Toast.makeText(LifePriceMainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                            }
                        })   //点击确定来删除
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //取消则不做任何操作
                            }
                        })
                        .create().show();
                break;
            }
            case 3:
                Toast.makeText(this, "版权所有by Wushenhao", Toast.LENGTH_SHORT).show();
                break;
        }
            return super.onContextItemSelected(item);
    }

    private void InitData() {
        theGoods=new ArrayList<Good>();
        theGoods.add(new Good("fish",1, R.drawable.a1));
        theGoods.add(new Good("fish 2",2,R.drawable.a2));
        theGoods.add(new Good("fish 3",3,R.drawable.a3));
    }

    protected class GoodsArrayAdapter extends ArrayAdapter<Good>
    {
        private  int resourceId;
        public GoodsArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Good> objects) {
            super(context, resource, objects);
            resourceId=resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater mInflater= LayoutInflater.from(this.getContext());
            View item = mInflater.inflate(this.resourceId,null);

            ImageView img = (ImageView)item.findViewById(R.id.image_view_good);
            TextView name = (TextView)item.findViewById(R.id.text_view_name);
            TextView price = (TextView)item.findViewById(R.id.text_view_price);

            Good good_item = this.getItem(position);
            img.setImageResource(good_item.getPictureId());
            name.setText("商品："+good_item.getName());
            price.setText("价格："+good_item.getPrice()+"元");

            return item;
        }
    }
}
