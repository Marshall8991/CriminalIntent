package com.javernaut.criminalintent.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.javernaut.criminalintent.R;
import com.javernaut.criminalintent.details.CrimeDetailsFragment;
import com.javernaut.criminalintent.model.Crime;
import com.javernaut.criminalintent.repository.Repository;
import com.javernaut.criminalintent.repository.RepositoryProvider;

public class CrimeListFragment extends Fragment {

    // View
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crime_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new CrimeListAdapter(
                RepositoryProvider.getInstance().getAllCrimes(), itemEventsListener
        ));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.crime_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.createNew) {
            RepositoryProvider.getInstance().generateRandomCrime();
            recyclerView.getAdapter().notifyDataSetChanged();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private final CrimeListAdapter.ItemEventsListener itemEventsListener = new CrimeListAdapter.ItemEventsListener() {
        @Override
        public void onItemClick(Crime crime) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, CrimeDetailsFragment.makeInstance(crime.getId()))
                    .addToBackStack(null)
                    .commit();
        }

        @Override
        public void onLongItemClick(Crime crime) {
            RepositoryProvider.getInstance().delete(crime);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    };
}
