package com.oshanak.mobilemarket.Activity.Common;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.widget.AppCompatTextView;

import com.oshanak.mobilemarket.R;

import java.util.ArrayList;
import java.util.List;

public class SearchableSpinner extends AppCompatTextView {

    private List<String> items = new ArrayList<>();
    private String selectedItem = "";
    private OnItemSelectedListener listener;

    public interface OnItemSelectedListener {
        void onItemSelected(String item, int position);
    }

    public SearchableSpinner(Context context) {
        super(context);
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setPadding(16, 16, 16, 16);
        setBackgroundResource(R.drawable.edit_text_style_normal1);
        setOnClickListener(v -> showSearchDialog());
    }

    public void setItems(List<String> itemList) {
        this.items = itemList;
        if (!items.isEmpty()) {
            setText(items.get(0));
            selectedItem = items.get(0);
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener l) {
        this.listener = l;
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_searchable_spinner, null);
        builder.setView(view);

        EditText searchBox = view.findViewById(R.id.search_box);
        ListView listView = view.findViewById(R.id.list_view);

        // آداپتر سفارشی با فیلتر فارسی‌ساز
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.simplecustomlist, new ArrayList<>(items)) {
            List<String> filteredList = new ArrayList<>();

            @Override
            public android.widget.Filter getFilter() {
                return new android.widget.Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults results = new FilterResults();
                        filteredList.clear();

                        if (constraint == null || constraint.length() == 0) {
                            filteredList.addAll(items);
                        } else {
                            String filterPattern = convertPersianToEnglish(constraint.toString().toLowerCase().trim());
                            for (String item : items) {
                                String normalizedItem = convertPersianToEnglish(item.toLowerCase());
                                if (normalizedItem.contains(filterPattern)) {
                                    filteredList.add(item);
                                }
                            }
                        }

                        results.values = filteredList;
                        results.count = filteredList.size();
                        return results;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        clear();
                        addAll((List<String>) results.values);
                        notifyDataSetChanged();
                    }

                    @Override
                    public CharSequence convertResultToString(Object resultValue) {
                        return resultValue.toString();
                    }
                };
            }
        };

        listView.setAdapter(adapter);

        AlertDialog dialog = builder.create();

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
            @Override public void afterTextChanged(Editable s) { }
        });

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            String item = adapter.getItem(position);
            setText(item);
            selectedItem = item;
            if (listener != null) {
                listener.onItemSelected(item, items.indexOf(item));
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    // تبدیل اعداد فارسی به انگلیسی
    private String convertPersianToEnglish(String input) {
        if (input == null) return null;
        return input
                .replace("۰", "0")
                .replace("۱", "1")
                .replace("۲", "2")
                .replace("۳", "3")
                .replace("۴", "4")
                .replace("۵", "5")
                .replace("۶", "6")
                .replace("۷", "7")
                .replace("۸", "8")
                .replace("۹", "9");
    }
}
