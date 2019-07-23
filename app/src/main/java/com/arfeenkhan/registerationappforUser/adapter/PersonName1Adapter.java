package com.arfeenkhan.registerationappforUser.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arfeenkhan.registerationappforUser.activity.UserRegistertionActive;
import com.arfeenkhan.registerationappforUser.model.SessionNameModel;
import com.arfeenkhan.registerationappforUser.R;
import com.arfeenkhan.registerationappforUser.utils.Common;

import java.util.ArrayList;

public class PersonName1Adapter extends RecyclerView.Adapter<PersonName1Adapter.PeopleViewHolder> {
    ArrayList<SessionNameModel> list;
    Context context;
    private int highlightItem = 0;

    String delete_allocation_name = "http://magicconversion.com/barcodescanner/deleteAllocationName.php";

    public PersonName1Adapter(ArrayList<SessionNameModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public PeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_name_item, parent, false);
        return new PeopleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PeopleViewHolder holder, int position) {
        final SessionNameModel ssm = list.get(position);
        holder.txtName.setText(ssm.getName());

        Common.editsessionid = ssm.getId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = holder.txtName.getText().toString();

                Intent startUserRegisterIntent = new Intent(context, UserRegistertionActive.class);
                startUserRegisterIntent.putExtra("name", name);
                context.startActivity(startUserRegisterIntent);
            }
        });
    }


//    private void delete() {
//        RequestQueue queue = Volley.newRequestQueue(context);
//        StringRequest sr = new StringRequest(Request.Method.POST, delete_allocation_name, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONArray arr = new JSONArray(response);
//                    JSONObject c = arr.getJSONObject(0);
//                    String mesaage = c.getString("message");
//                    Toast.makeText(context, mesaage, Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("id", Common.editsessionid);
//                return params;
//            }
//        };
//
//        queue.add(sr);
//    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PeopleViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView checkImage;

        public PeopleViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.person_name);
        }
    }
}
