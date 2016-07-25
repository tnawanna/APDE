import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;
import org.eclipse.jgit.treewalk.FileTreeIterator;
public class GitHistoryActivity extends AppCompatActivity {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setBackgroundColor(getResources().getColor(R.color.bar_overlay));
		setSupportActionBar(toolbar);
		
		commitMessages = repo.getRecentCommitMessages(commits, 72);
		commitMessages.add(0, "[Local Changes]");
		commitMessages.add("[Empty Repository]");
		if (getResources().getBoolean(R.bool.tablet_multi_pane)) {
	public boolean isMultiPane() {
		return getResources().getBoolean(R.bool.tablet_multi_pane);
	}
	
		if (num == commitMessages.size() - 1) {
			//You can't select the last item, "Empty Repository"
			return;
		}

		if (isMultiPane()) {
			commitListFragment.selectItem(num);
		}
		
		diffCommits(num, num + 1, num > 0 ? commits.get(num - 1) : null);
	}
	
	protected void diffCommits(int a, int b, final RevCommit commit) {
		if (!(isMultiPane())) {
		final int commitNum = Math.min(a, b);
		final int parentNum = Math.max(a, b);
		
		loadThread = new Thread() {
			@Override
				
				AbstractTreeIterator commitTreeIterator;
				AbstractTreeIterator parentTreeIterator;
				
				if (commitNum == 0) {
					//Local changes
					commitTreeIterator = new FileTreeIterator(repo.getGit().getRepository());
				} else {
					//Some commit
					commitTreeIterator = createTreeIterator(commits.get(commitNum - 1).getTree());
				}
				
				if (parentNum == commits.size() + 1) {
					//Empty repository
					parentTreeIterator = new EmptyTreeIterator();
					//Some commit
					parentTreeIterator = createTreeIterator(commits.get(parentNum - 1).getTree());
					List<DiffEntry> diffEntries;
					
					diffEntries = formatter.scan(parentTreeIterator, commitTreeIterator);
					

							commitDiffFragment.setCommitDiffs(commit, commitDiffs);
		};

	private CanonicalTreeParser createTreeIterator(RevTree tree) {
		CanonicalTreeParser treeParser = new CanonicalTreeParser();
		ObjectReader objectReader = repo.getGit().getRepository().newObjectReader();
		
		try {
			treeParser.reset(objectReader, tree.getId());
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			objectReader.release();
		}
		
		return treeParser;
	}
	
//		if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB)
//			startActivity(new Intent(this, SettingsActivity.class));
//		else
		private int[] selection = {-1, -1};
		
		private ActionMode actionMode;
			// StackOverflow: http://stackoverflow.com/a/23533575
		@Override
		public void onDestroyView() {
			// StackOverflow: http://stackoverflow.com/a/23533575
			if (rootView.getParent() != null) {
				((ViewGroup) rootView.getParent()).removeView(rootView);
			}
			
			super.onDestroyView();
		}
		
				
				
					
					
					
						
						if (selectedItem != position) {
							for (int item : selection) {
								if (position == item) {
									convertView.setBackgroundColor(getResources().getColor(R.color.holo_select_light));
								}
							}
						}
						
						
						
				
						if (actionMode == null) {
							((GitHistoryActivity) getActivity()).selectCommit(position);
						} else {
							handleActionModeTouch(position);
						}
					}
				});
				
				commitList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
						handleActionModeTouch(position);
						
						return true;
		private void handleActionModeTouch(int position) {
			//If this item has already been selected, then un-select it
			//If there are less than two items selected, then select this item
			//If there are already two items selected, then tell the user that they can't select another item
			if (selection[0] == position) {
				selection[0] = -1;
			} else if (selection[1] == position) {
				selection[1] = -1;
			} else if (selection[0] == -1) {
				selection[0] = position;
			} else if (selection[1] == -1) {
				selection[1] = position;
			} else {
				Toast.makeText(getActivity(), R.string.git_diff_two_items, Toast.LENGTH_SHORT).show();
			}
			
			selectItems();
			
			if ((selection[0] != -1 || selection[1] != -1) && actionMode == null) {
				//We need to start the CAB
				
				actionMode = ((GitHistoryActivity) getActivity()).startSupportActionMode(new ActionMode.Callback() {
					@Override
					public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
						actionMode.getMenuInflater().inflate(R.menu.git_history_commit_list_actions, menu);
						return true;
					}
					
					@Override
					public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
						return false;
					}
					
					@Override
					public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
						switch (menuItem.getItemId()) {
						case R.id.menu_git_diff:
							//Need two commits for a diff...
							if (selection[0] != -1 && selection[1] != -1) {
								((GitHistoryActivity) getActivity()).diffCommits(selection[0], selection[1], null);
								actionMode.finish();
							} else {
								Toast.makeText(getActivity(), R.string.git_diff_two_items, Toast.LENGTH_SHORT).show();
							}
							
							return true;
						}
						
						return false;
					}
					
					@Override
					public void onDestroyActionMode(ActionMode actionMode) {
						selection[0] = -1;
						selection[1] = -1;
						
						//If we're navigating away, then we can't be messing with the non-existant view...
						if (getView() != null) {
							selectItems();
						}
						
						CommitListFragment.this.actionMode = null;
					}
				});
			} else if (selection[0] == -1 && selection[1] == -1 && actionMode != null) {
				//We need to close the CAB
				
				actionMode.finish();
			}
		}
		
			//Keep the selected commit on screen... with a little bit of breathing room
			if (num < commitList.getFirstVisiblePosition() + 2) {
				commitList.setSelection(num == 0 ? num : num - 1);
			} else if (num > commitList.getLastVisiblePosition() - 2) {
				commitList.setSelection(num == commitList.getCount() - 1 ? num : num + 1);
		
		public void selectItems() {
			final ListView commitList = (ListView) getView().findViewById(R.id.git_history_commit_list);
			
			for (int i = 0; i < commitList.getCount(); i ++) {
				View child = commitList.getChildAt(i);
				
				if (child != null) {
					child.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				}
			}
			
			for (int item : selection) {
				int index = item - commitList.getFirstVisiblePosition();
				View child = commitList.getChildAt(index);
				
				if (child != null) {
					child.setBackgroundColor(getResources().getColor(R.color.holo_select_light));
				}
			}
		}
	 
		public void setCommitDiffs(RevCommit commit, final ArrayList<CommitDiff> commitDiffs) {
			
			if (commit != null) {
				PersonIdent commitAuthor = commit.getAuthorIdent();
				String name = commitAuthor.getName();
				String email = commitAuthor.getEmailAddress();
				
				String author;
				
				if (name.equals("") && email.equals("")) {
					author = "No Author";
				} else if (name.equals("")) {
					author = "<" + email + ">";
				} else if (email.equals("")) {
					author = name;
				} else {
					author = name + " <" + email + ">";
				}
				
				String timestamp = commit.getCommitterIdent().getWhen().toString();
				
				final String shortMessage = GitRepository.ellipsizeCommitMessage(commit, 72);
				final String fullMessage = commit.getFullMessage();
				
				final TextView messageView = ((TextView) getView().findViewById(R.id.git_history_diff_commit_message));
				final TextView authorView = ((TextView) getView().findViewById(R.id.git_history_diff_commit_author));
				final TextView timestampView = ((TextView) getView().findViewById(R.id.git_history_diff_commit_timestamp));
				
				messageView.setText(shortMessage);
				authorView.setText(author);
				timestampView.setText(timestamp);
				
				View diffHeader = getView().findViewById(R.id.git_history_diff_header);
				diffHeader.setVisibility(View.VISIBLE);
				
				if (!shortMessage.equals(fullMessage)) {
					//Allow the user to toggle between the full message and the shortened message
					View.OnClickListener messageToggleListener = new View.OnClickListener() {
						private boolean full = false;
						
						@Override
						public void onClick(View view) {
							full = !full;
							messageView.setText(full ? fullMessage : shortMessage);
						}
					};
					
					//Detect touches anywhere in the header
					diffHeader.setOnClickListener(messageToggleListener);
					messageView.setOnClickListener(messageToggleListener);
					authorView.setOnClickListener(messageToggleListener);
					timestampView.setOnClickListener(messageToggleListener);
				}
			}
			