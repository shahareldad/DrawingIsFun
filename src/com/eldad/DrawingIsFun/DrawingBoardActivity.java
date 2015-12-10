package com.eldad.DrawingIsFun;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.UUID;

public class DrawingBoardActivity extends Activity implements View.OnClickListener {

    private final String TAG = "DrawingBoardActivity";

    private ImageButton _currentPaint, _drawButton, _eraseButton, _newButton, _saveButton;
    private DrawingView _drawingView;
    private float _smallBrush, _mediumBrush, _largeBrush;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        _smallBrush = getResources().getInteger(R.integer.small_size);
        _mediumBrush = getResources().getInteger(R.integer.medium_size);
        _largeBrush = getResources().getInteger(R.integer.large_size);

        _drawingView = (DrawingView)findViewById(R.id.drawingArea);
        _drawButton = (ImageButton)findViewById(R.id.draw_btn);
        _eraseButton = (ImageButton)findViewById(R.id.eraser_btn);
        _newButton = (ImageButton)findViewById(R.id.new_btn);
        _saveButton = (ImageButton)findViewById(R.id.save_btn);
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);

        _currentPaint = (ImageButton)paintLayout.getChildAt(0);
        _currentPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        _drawButton.setOnClickListener(this);
        _eraseButton.setOnClickListener(this);
        _newButton.setOnClickListener(this);
        _saveButton.setOnClickListener(this);
    }

    public void paintClicked(View view){
        if (view != _currentPaint){

            String color = view.getTag().toString();
            _drawingView.setColor(color);

            ImageButton imgView = (ImageButton)view;
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            _currentPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            _currentPaint = (ImageButton)view;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.draw_btn){
            DrawingButtonClicked();
        }
        if (v.getId() == R.id.eraser_btn){
            EraserButtonClicked();
        }
        if (v.getId() == R.id.new_btn){
            NewButtonClicked();
        }
        if (v.getId() == R.id.save_btn){
            SaveButtonClicked();
        }
    }

    private void NewButtonClicked() {
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle(getString(R.string.newDrawing));
        newDialog.setMessage(getString(R.string.startNewDrawingConfirmationMessage));
        newDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                _drawingView.StartNew();
                dialog.dismiss();
            }
        });
        newDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        newDialog.show();
    }

    private void SaveButtonClicked() {
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle(getString(R.string.saveDrawing));
        saveDialog.setMessage(getString(R.string.saveDrawingComfirmationMessage));
        saveDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                _drawingView.setDrawingCacheEnabled(true);
                String imgSaved = MediaStore.Images.Media.insertImage(
                        getContentResolver(), _drawingView.getDrawingCache(),
                        UUID.randomUUID().toString() + getString(R.string.fileExtension), getString(R.string.fileDecription));
                if (imgSaved != null)
                    Toast.makeText(getApplicationContext(), getString(R.string.drawingSaved), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), getString(R.string.failedToSave), Toast.LENGTH_SHORT).show();
                _drawingView.destroyDrawingCache();
            }
        });
        saveDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        saveDialog.show();
    }

    private void EraserButtonClicked() {
        final Dialog brushDialog = GetDialog(R.string.eraserSizeDialogTitle, R.layout.brush_chooser);

        ImageButton smallButton = (ImageButton)brushDialog.findViewById(R.id.small_brush);
        smallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _drawingView.SetBrushSize(_smallBrush);
                _drawingView.SetEraseMode(true);
                brushDialog.dismiss();
            }
        });

        ImageButton mediumButton = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _drawingView.SetBrushSize(_mediumBrush);
                _drawingView.SetEraseMode(true);
                brushDialog.dismiss();
            }
        });

        ImageButton lasrgeButton = (ImageButton)brushDialog.findViewById(R.id.large_brush);
        lasrgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _drawingView.SetBrushSize(_largeBrush);
                _drawingView.SetEraseMode(true);
                brushDialog.dismiss();
            }
        });

        brushDialog.show();
    }

    private void DrawingButtonClicked() {
        final Dialog brushDialog = GetDialog(R.string.brushSizeDialogTitle, R.layout.brush_chooser);

        ImageButton smallButton = (ImageButton)brushDialog.findViewById(R.id.small_brush);
        smallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _drawingView.SetBrushSize(_smallBrush);
                _drawingView.SetLastBrushSize(_drawingView.GetLastBrushSize());
                _drawingView.SetEraseMode(false);
                brushDialog.dismiss();
            }
        });

        ImageButton mediumButton = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _drawingView.SetBrushSize(_mediumBrush);
                _drawingView.SetLastBrushSize(_drawingView.GetLastBrushSize());
                _drawingView.SetEraseMode(false);
                brushDialog.dismiss();
            }
        });

        ImageButton lasrgeButton = (ImageButton)brushDialog.findViewById(R.id.large_brush);
        lasrgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _drawingView.SetBrushSize(_largeBrush);
                _drawingView.SetLastBrushSize(_drawingView.GetLastBrushSize());
                _drawingView.SetEraseMode(false);
                brushDialog.dismiss();
            }
        });

        brushDialog.show();
    }

    private Dialog GetDialog(int title, int layout) {
        final Dialog brushDialog = new Dialog(this);
        brushDialog.setTitle(getString(title));
        brushDialog.setContentView(layout);
        return brushDialog;
    }
}
