package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI()
    {
        CrimeLab crimeLab  = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
    }

    // в новой Android Studio  нужно использовать (androidx)
    // androidx.recyclerview.widget.RecyclerView
    // https://stackoverflow.com/questions/25477860/error-inflating-class-android-support-v7-widget-recyclerview
    // и добавить библиотеку androidx.recyclerview:1.1.0
    // .

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Crime       mCrime;
        private TextView    mTitleTextView;
        private TextView    mDateTextView;
        private ImageView   mSolvedImageView;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);
            //
            mTitleTextView   = itemView.findViewById(R.id.crime_title);
            mDateTextView    = itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);
        }

        public void bind(Crime crime)
        {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            // упражнение стр. 220
            String dateString = DateFormat.format("EEEE, MMM d, yyyy", mCrime.getDate()).toString();
            mDateTextView.setText(dateString);
            //
            mSolvedImageView.setVisibility((mCrime.isSolved()? View.VISIBLE: View.GONE));
        }

        @Override
        public void onClick(View v) {
            /// Toast.makeText(getActivity(), mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes)
        {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            Log.d("CrimeAdapter", "onCreateViewHolder(" + viewType + ") called ");
            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
            Log.d("CrimeAdapter", "onBindViewHolder(" + position + ") called ");
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

}
