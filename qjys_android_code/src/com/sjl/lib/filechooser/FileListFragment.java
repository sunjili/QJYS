/*
 * Copyright (C) 2013 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sjl.lib.filechooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.rmtech.qjys.R;

/**
 * Fragment that displays a list of Files in a given path.
 * 
 * @version 2013-12-11
 * @author jilisun
 */
public class FileListFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<List<File>> {

	/**
	 * Interface to listen for events.
	 */
	public interface Callbacks {
		/**
		 * Called when a file is selected from the list.
		 * 
		 * @param file
		 *            The file selected
		 */
		public void onFileSelected(File file);
	}

	private static final int LOADER_ID = 0;

	private FileListAdapter mAdapter;
	private String mPath;

	private Callbacks mListener;

	/**
	 * Create a new instance with the given file path.
	 * 
	 * @param path
	 *            The absolute path of the file (directory) to display.
	 * @return A new Fragment with the given file path.
	 */
	public static FileListFragment newInstance(String path) {
		FileListFragment fragment = new FileListFragment();
		Bundle args = new Bundle();
		args.putString(FileChooserActivity.PATH, path);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mListener = (Callbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement FileListFragment.Callbacks");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new FileListAdapter(getActivity());
		mPath = getArguments() != null ? getArguments().getString(
				FileChooserActivity.PATH) : Environment
				.getExternalStorageDirectory().getAbsolutePath();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		setEmptyText(getString(R.string.empty_directory));
        Drawable divider = getContext().getResources().getDrawable(R.color.cd9);
        this.getListView().setDivider(divider);
        this.getListView().setDividerHeight(1);//必须要把setDividerHeight放在setDivider的后面
		setListAdapter(mAdapter);
		setListShown(false);

		getLoaderManager().initLoader(LOADER_ID, null, this);

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		FileListAdapter adapter = (FileListAdapter) l.getAdapter();
		if (adapter != null) {
			File file = (File) adapter.getItem(position);

			if (!file.isDirectory()
					&& FileUtils.isImageFileType(file.getAbsolutePath())) {
				ImageView cbox = (ImageView) v.findViewById(R.id.checkbox);
				ArrayList<String> resultList = ((FileChooserActivity) getActivity())
						.getResultList();
				String path = file.getAbsolutePath();
				if (resultList.contains(path)) {
					cbox.setImageResource(R.drawable.btn_choice_nor);
				} else {
					cbox.setImageResource(R.drawable.btn_choice_press);
					mPath = path;
				}
			}
			mListener.onFileSelected(file);
		}
	}

	@Override
	public Loader<List<File>> onCreateLoader(int id, Bundle args) {
		return new FileLoader(getActivity(), mPath);
	}

	@Override
	public void onLoadFinished(Loader<List<File>> loader, List<File> data) {
		mAdapter.setListItems(data);

		if (isResumed())
			setListShown(true);
		else
			setListShownNoAnimation(true);
	}

	@Override
	public void onLoaderReset(Loader<List<File>> loader) {
		mAdapter.clear();
	}
}
