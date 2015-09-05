package arya.spliro.custom;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import arya.spliro.R;
import arya.spliro.adapters.CategoryListAdaptor;
import arya.spliro.custom.CustomDialog;
import arya.spliro.dao.Categories;

/**
 * Created by Admin on 8/26/2015.
 */
public class CategoryListDialog extends CustomDialog implements View.OnClickListener,TextWatcher
{
    View view;
    ListView categorylist;

    ArrayList<String> catName;
    CategoryListAdaptor catAdaptor;
    ImageView imgBtnSimpleListBack;
    ArrayList<Categories> categoriesArrayList;
    ArrayList<Categories> categorySearchList;
    EditText eTextSimpleListSearch;
    ImageView imgBtnSListClearSearch;
    boolean categoryclickable;
    public static CategoryListDialog getInstance(ArrayList<Categories> categoriesArrayList,boolean categoryclickable)
    {
        CategoryListDialog dialog_cat=new CategoryListDialog();
        dialog_cat.categoriesArrayList=categoriesArrayList;
        dialog_cat.categoryclickable=categoryclickable;
        return  dialog_cat;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.category_list_dialog,container, false);
        init();
        return view;
    }
    private void init()
    {
        categorylist=(ListView)view.findViewById(R.id.listCategoryList);
        imgBtnSListClearSearch=(ImageView)view.findViewById(R.id.imgBtnSListClearSearch);
        imgBtnSimpleListBack=(ImageView)view.findViewById(R.id.imgBtnSimpleListBack);
        eTextSimpleListSearch=(EditText)view.findViewById(R.id.eTextSimpleListSearch);
        imgBtnSimpleListBack.setOnClickListener(this);
        eTextSimpleListSearch.addTextChangedListener(this);
        imgBtnSListClearSearch.setOnClickListener(this);
        categorySearchList=new ArrayList<Categories>();
        setAdapter(false);
    }
    private void setAdapter(boolean isSearch)
    {
        if(isSearch)
        {
            catAdaptor=new CategoryListAdaptor(getActivity(),categorySearchList,categoryclickable);
        }
        else
        {
            catAdaptor=new CategoryListAdaptor(getActivity(),categoriesArrayList,categoryclickable);
        }
        categorylist.setAdapter(catAdaptor);
        catAdaptor.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view)
    {
            if(view.getId()== R.id.imgBtnSimpleListBack)
            {
               dismiss();
            }
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        try
        {
            int textlength = eTextSimpleListSearch.getText().toString().length();
            if (textlength > 0)
            {
                categorySearchList.clear();
                for (int i = 0; i < categoriesArrayList.size(); i++)
                {
                    if (categoriesArrayList.get(i).name != null)
                    {
                        if (categoriesArrayList.get(i).name.toString().toLowerCase().contains(((CharSequence) eTextSimpleListSearch.getText().toString().toLowerCase().subSequence(0, textlength))))
                        {
                            categorySearchList.add(categoriesArrayList.get(i));
                        }
                    }
                }
                setAdapter(true);
            }
            else
            {
                setAdapter(false);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {}
}
