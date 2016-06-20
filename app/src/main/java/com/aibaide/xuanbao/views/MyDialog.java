package com.aibaide.xuanbao.views;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.sunshine.utils.DensityUtils;

public class MyDialog extends Dialog {

	public MyDialog(Context context, int theme) {
		super(context, theme);
	}

	public MyDialog(Context context) {
		super(context);
	}

	public static int BLUE = R.color.tab_check;

	public static class Builder {
		private Context context;

		private String title;

		private String message;

		private int left, right, top, bottom;

		private int buttonId = -1;

		String[] items = null;

		private int titleTextColor = Integer.MAX_VALUE;

		private int buttonTextColor = Integer.MAX_VALUE;

		private int buttonbackgroundId = -1;

		private String positiveButtonText;

		private String negativeButtonText;

		private String neutralButtonText;

		private View contentView;
		private View mLayout;
		private TextView postiveButton;
		private boolean mCancelable = false;
		private OnClickListener positiveButtonClickListener,
				negativeButtonClickListener, neutralButtonClickListener,
				itemButtonListener;

		public Builder(Context context) {
			this.context = context;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mLayout = inflater.inflate(R.layout.my_dialog_layout, null);
		}

		public Builder(Context context, int color) {
			this.context = context;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mLayout = inflater.inflate(R.layout.my_dialog_layout, null);
			mLayout.findViewById(R.id.line2).setBackgroundColor(color);
			mLayout.findViewById(R.id.positiveButton).setBackgroundColor(color);
			mLayout.findViewById(R.id.neutralButton).setBackgroundColor(color);
			mLayout.findViewById(R.id.negativeButton).setBackgroundColor(color);

		}

		public void setCancelable(Boolean boolean1) {
			this.mCancelable = boolean1;
		}

		public Builder setTitleTextColor(int titleTextColor) {
			this.titleTextColor = titleTextColor;
			return this;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setButtonTextColor(int buttonTextColor) {
			this.buttonTextColor = buttonTextColor;
			return this;
		}

		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		public Builder setButtonBackground(int buttonbackgroundId) {
			this.buttonbackgroundId = buttonbackgroundId;
			return this;
		}

		public Builder setButtonTheme(int buttonId) {
			this.buttonId = buttonId;
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setView(View v) {
			this.contentView = v;
			return this;
		}

		public Builder setView(View v, int left, int top, int right, int bottom) {
			this.contentView = v;
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
			return this;
		}

		public Builder setItem(String items[], OnClickListener listener) {
			this.items = items;
			itemButtonListener = listener;
			return this;
		}

		public Builder setPositiveButton(int positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNeutralButton(int neutralButtonText,
				OnClickListener listener) {
			this.neutralButtonText = (String) context
					.getText(neutralButtonText);
			this.neutralButtonClickListener = listener;
			return this;
		}

		public Builder setNeutralButton(String neutralButtonText,
				OnClickListener listener) {
			this.neutralButtonText = neutralButtonText;
			this.neutralButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public MyDialog create() {
			final MyDialog Mydialog = new MyDialog(context, R.style.loading_dialog);
			Mydialog.setCancelable(mCancelable);
			// android.view.ViewGroup.LayoutParams layoutparams = layout
			// .getLayoutParams();
			// layoutparams.width = (int) (Variable.witdh - Util.dip2px(10));
			// layout.setLayoutParams(layoutparams);
			postiveButton = ((TextView) mLayout
					.findViewById(R.id.positiveButton));
			if (titleTextColor != Integer.MAX_VALUE) {
				((TextView) mLayout.findViewById(R.id.title))
						.setTextColor(titleTextColor);
			}
			if (buttonTextColor != Integer.MAX_VALUE) {
				postiveButton.setTextColor(buttonTextColor);
				((TextView) mLayout.findViewById(R.id.negativeButton))
						.setTextColor(buttonTextColor);
				((TextView) mLayout.findViewById(R.id.neutralButton))
						.setTextColor(buttonTextColor);
			}
			if (buttonId != -1) {
				postiveButton.setBackgroundResource(buttonId);
				mLayout.findViewById(R.id.negativeButton)
						.setBackgroundResource(buttonId);
				mLayout.findViewById(R.id.neutralButton)
						.setBackgroundResource(buttonId);
			}
			if (buttonbackgroundId != -1) {
				mLayout.findViewById(R.id.mydialogbuttonlayout)
						.setBackgroundResource(buttonbackgroundId);
			}
			Mydialog.addContentView(mLayout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			if (title != null) {

				((TextView) mLayout.findViewById(R.id.title)).setText(title);
			} else {
				mLayout.findViewById(R.id.title).setVisibility(View.GONE);
			}
			if (positiveButtonText != null) {
				postiveButton.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					postiveButton
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(
											Mydialog,
											DialogInterface.BUTTON_POSITIVE);
									postiveButton.setClickable(false);
									Mydialog.dismiss();
								}
							});
				}
			} else {
				mLayout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
			}
			if (neutralButtonText != null) {
				((TextView) mLayout.findViewById(R.id.neutralButton))
						.setText(neutralButtonText);
				if (neutralButtonClickListener != null) {
					mLayout.findViewById(R.id.neutralButton)
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									neutralButtonClickListener.onClick(
											Mydialog,
											DialogInterface.BUTTON_NEUTRAL);
									Mydialog.dismiss();
								}
							});
				}
			} else {
				mLayout.findViewById(R.id.neutralButton).setVisibility(
						View.GONE);
			}
			if (negativeButtonText != null) {
				((TextView) mLayout.findViewById(R.id.negativeButton))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					mLayout.findViewById(R.id.negativeButton)
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(
											Mydialog,
											DialogInterface.BUTTON_NEGATIVE);
									Mydialog.dismiss();
								}
							});
				}
			} else {
				mLayout.findViewById(R.id.negativeButton).setVisibility(
						View.GONE);
			}
			if (negativeButtonText == null && neutralButtonText == null
					&& positiveButtonText == null) {
				mLayout.findViewById(R.id.mydialogbuttonlayout)
						.setVisibility(View.GONE);
			}
			if (message != null) {
				((TextView) mLayout.findViewById(R.id.message))
						.setText(message);
			} else {
				mLayout.findViewById(R.id.message)
						.setVisibility(View.GONE);
			}
			if (items != null && items.length > 0) {
				LinearLayout selflayout = ((LinearLayout) mLayout
						.findViewById(R.id.content));
				selflayout.removeAllViews();
				for (int i = 0; i < items.length; i++) {
					final int pos = i;
					TextView text = new TextView(context);
					text.setLayoutParams(new LayoutParams(
							LayoutParams.FILL_PARENT, 80));
					text.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
					text.setTextSize(16);
					text.setTextColor(Color.BLACK);
					text.setBackgroundColor(Color.WHITE);
					text.setPadding(30, 0, 0, 0);
					text.setText(items[i]);
					selflayout.addView(text);
					if (i != items.length - 1) {
						ImageView textline = new ImageView(context);
						textline.setLayoutParams(new LayoutParams(
								LayoutParams.FILL_PARENT, DensityUtils.dp2px(
										context, 2)));
						textline.setBackgroundColor(Color.parseColor("#ff9000"));
						selflayout.addView(textline);
					}
					if (itemButtonListener != null) {
						text.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								itemButtonListener.onClick(Mydialog, pos);
								Mydialog.dismiss();
							}
						});
					}
				}
			} else if (contentView != null) {
				((LinearLayout) mLayout.findViewById(R.id.content))
						.removeAllViews();
				mLayout.findViewById(R.id.content).setPadding(
						left, top, right, bottom);
				((LinearLayout) mLayout.findViewById(R.id.content)).addView(
						contentView, new LayoutParams(LayoutParams.FILL_PARENT,
								LayoutParams.WRAP_CONTENT));
			}
			Mydialog.setContentView(mLayout, new LayoutParams(
					((Activity) context).getWindowManager()
							.getDefaultDisplay().getWidth() * 4 / 5,
					LayoutParams.WRAP_CONTENT));
			return Mydialog;
		}
	}

}
