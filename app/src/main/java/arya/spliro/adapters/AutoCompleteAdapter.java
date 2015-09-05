package arya.spliro.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import arya.spliro.R;
import arya.spliro.dao.Categories;
import arya.spliro.viewController.SpliroApp;

public class AutoCompleteAdapter extends BaseAdapter implements
		Filterable {
    private LayoutInflater inflate;
    private Context mcontext;
    private ArrayList<Categories> catArrayList;
	private ArrayList<Categories> mOriginalValues;
	private ArrayFilter mFilter;


	public AutoCompleteAdapter(Context mcontext, ArrayList<Categories> catArrayList) {
		this.catArrayList = catArrayList;
		this.mOriginalValues = (ArrayList<Categories>) catArrayList.clone();
        this.mcontext=mcontext;
        inflate = LayoutInflater.from(mcontext);
	}

	@Override
	public int getCount() {
		return catArrayList.size();
	}

	@Override
	public Categories getItem(int position) {
		return catArrayList.get(position);
	}

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AutoCompleteViewHolder holder;
        if (convertView == null) {
            holder = new AutoCompleteViewHolder();
            convertView = inflate.inflate(R.layout.auto_complete_row, parent, false);
            holder.txtCatName=(TextView)convertView.findViewById(R.id.txtCatName);
            convertView.setTag(holder);
        } else {
            holder = (AutoCompleteViewHolder) convertView.getTag();
        }

        holder.txtCatName.setTypeface(SpliroApp.getFontLight());

        Categories data =catArrayList.get(position);

        if(data.name!=null)
        {

            holder.txtCatName.setText(data.name);
        }


        return convertView;
    }


    @Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

    private class ArrayFilter extends Filter {

               public ArrayFilter() {
                           }

              @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults oReturn = new FilterResults();

                      ArrayList<Categories> results = new ArrayList<Categories>();

                      if (mOriginalValues == null)

                          mOriginalValues = catArrayList;



                     if (constraint != null)

                    {

                           if (mOriginalValues != null && mOriginalValues.size() > 0) {

                                  for (Categories g : mOriginalValues) {

                                           if (g.name.toLowerCase().contains(constraint.toString().toLowerCase()))

                                                      results.add(g);

                                        }

                                 }

                              oReturn.values = results;

                            }
                  else
                     {
                         oReturn.values = results;
                     }

                       return oReturn;

                     }



                   @SuppressWarnings("unchecked")

            @Override

                protected void publishResults(CharSequence constraint, FilterResults results) {

                       catArrayList = (ArrayList<Categories>)results.values;

                      notifyDataSetChanged();

                     }

           }

    public class AutoCompleteViewHolder
    {
        TextView txtCatName;
    }
}