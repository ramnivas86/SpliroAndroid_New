package arya.spliro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import arya.spliro.R;
import arya.spliro.dao.Categories;

/**
 * Created by Admin on 8/26/2015.
 */
public class CategoryListAdaptor extends BaseAdapter
{
    private LayoutInflater inflate;
    private Context mcontext;

    private View.OnClickListener clickListener;
    ArrayList<Categories> categoriesArrayList;
    boolean categoryclickable;

    public CategoryListAdaptor(Context mcontext, ArrayList<Categories> categoriesArrayList,boolean categoryclickable) {
        this.mcontext = mcontext;
        inflate = LayoutInflater.from(mcontext);
        this.categoriesArrayList=categoriesArrayList;
        this.categoryclickable=categoryclickable;
        this.clickListener=clickListener;
    }




    @Override
    public int getCount()
    {
        return categoriesArrayList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return categoriesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
            final CategoryRowHolder holder;
            if(convertView==null)
            {
                holder = new CategoryRowHolder();
                convertView = inflate.inflate(R.layout.category_row, parent, false);
                holder.txtCategoryName=(TextView)convertView.findViewById(R.id.txtCategoryName);
                holder.chkCategory=(CheckBox)convertView.findViewById(R.id.chkCategory);
                holder.chkCategory.setEnabled(categoryclickable);

                convertView.setTag(holder);

                if(categoriesArrayList.get(position).is_selected)
                {
                    holder.chkCategory.setChecked(true);
                }
                else
                {
                    holder.chkCategory.setChecked(false);
                }
                holder.chkCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(holder.chkCategory.isChecked())
                        {
                            categoriesArrayList.get(position).is_selected = true;
                            holder.chkCategory.setChecked(true);
                        }
                        else
                        {
                            categoriesArrayList.get(position).is_selected = false;
                            holder.chkCategory.setChecked(false);
                        }

                    }
                });
            }
            else
            {
                holder = (CategoryRowHolder) convertView.getTag();
            }
            Categories categories=categoriesArrayList.get(position);
            holder.txtCategoryName.setText(categories.name);
            return convertView;




    }

    class CategoryRowHolder
    {
        private CheckBox chkCategory;
        private TextView txtCategoryName;


    }

}
