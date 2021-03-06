package org.seven.component;

import org.seven.activity.R;
import org.seven.activity.StoreInfoGeoActivity;
import org.seven.conn.RequestTask;
import org.seven.data.LoginInfo;
import org.seven.utils.GlobalParam;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class KaoqinFragment extends Fragment implements OnClickListener {

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case GlobalParam.GETSTORES_SUCCESS: {
					Intent i = new Intent(GlobalParam.getInstance(),StoreInfoGeoActivity.class);
					Bundle b = msg.getData();
					b.putString("src", "kaoqin");
	            	i.putExtras(b);
	            	startActivity(i);
	            	dialog.dismiss();
					break;
				}
				case -1: {
					dialog.dismiss();
					String s = msg.getData().getString("message");
	            	AlertDialog ad = new AlertDialog.Builder(KaoqinFragment.this.getView().getContext()).setPositiveButton("ȷ��", null).create();
	            	ad.setIcon(android.R.drawable.ic_dialog_alert);
	            	ad.setTitle(R.string.error_title);
	            	ad.setMessage(s);
	            	ad.show();
					break;
				}
				case 2: {
					// request get store again.
					dialog.dismiss();
					LoginInfo loginInfo = GlobalParam.getInstance().GetLastLoginInfo();
					final RequestTask rt = new RequestTask();
					KaoqinFragment.this.dialog = new ProgressDialog(KaoqinFragment.this.getView()
							.getContext()) {
						protected void onStop() {
							rt.cancel();
						}
					};
					KaoqinFragment.this.dialog.setMessage(getString(R.string.request));
					KaoqinFragment.this.dialog.setCancelable(true);
					KaoqinFragment.this.dialog.show();
					rt.getStoreContainsGeo(loginInfo.getSid(), loginInfo.getUserno(), mHandler);
					break;
				}
				default:
			}
		};
	};
	
	private ProgressDialog dialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.kaoqin, container, false);
		Button button1 = (Button) view.findViewById(R.id.kaoqin1);
		button1.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.kaoqin1) {
			LoginInfo loginInfo = GlobalParam.getInstance().GetLastLoginInfo();
			if (loginInfo.getSid() != null && loginInfo.getUserno() != null) {
				final RequestTask rt = new RequestTask();
				this.dialog = new ProgressDialog(this.getView()
						.getContext()) {
					protected void onStop() {
						rt.cancel();
					}
				};
				this.dialog.setMessage(getString(R.string.request));
				this.dialog.setCancelable(true);
				this.dialog.show();
				rt.getStoreContainsGeo(loginInfo.getSid(), loginInfo.getUserno(), mHandler);
			} else {
				AlertDialog ad = new AlertDialog.Builder(this.getView()
						.getContext()).create();
				ad.setButton(getString(R.string.ok),
						Message.obtain(new Handler()));
				ad.setIcon(android.R.drawable.ic_dialog_alert);
				ad.setTitle(R.string.error_title);
				ad.setMessage("���ȵ�¼");
				ad.show();
			}
		}
	}
}
