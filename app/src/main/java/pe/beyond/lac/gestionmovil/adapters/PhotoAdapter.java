package pe.beyond.lac.gestionmovil.adapters;

import java.io.File;
import java.util.List;

import org.simpleframework.xml.stream.Position;

import pe.beyond.gls.model.Photo;
import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.utils.Constants;
import pe.beyond.lac.gestionmovil.utils.lazycache.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoAdapter extends BaseAdapter
{
	private LayoutInflater phInflater;
	private Context context;
	private String strRootPath;
	
	private List<Photo> photos;
	//public ImageLoader imageLoader;
	public ImageView imageView;
	
	public PhotoAdapter(Context context) {
		phInflater = LayoutInflater.from(context);
		this.context = context;
		//imageLoader = new ImageLoader(context);
		imageView = new ImageView(context);
		strRootPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
		
		
	}
	
	public List<Photo> getPhotos()
	{
		return photos;
	}
	
	public void setPhotos(List<Photo> photos)
	{
		this.photos = photos;
	}
	
	@Override
	public int getCount() {
		return (photos != null ? photos.size() : 0);
	}

	@Override
	public Object getItem(int position) {
		return photos.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		Holder holder;
		if (convertView == null) 
		{
			convertView = phInflater.inflate(R.layout.photo_item, null);
			holder = new Holder();
			
			holder.imgPhotoImage  = (ImageView) convertView.findViewById(R.id.imgPhotoImage);
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (Holder) convertView.getTag();
		}
		
		Photo photo =  photos.get(position);
		
		String strPhotoName = strRootPath + "/" +photo.getStrAbrevName();
		if (strPhotoName != null) 
		{
			File imgFile = new  File(strPhotoName);
			
			if (imgFile.exists())
			{
			    BitmapFactory.Options bounds = new BitmapFactory.Options();
			    bounds.inJustDecodeBounds = true;
			    BitmapFactory.decodeFile(imgFile.getPath(), bounds);
			    if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
			        return null;

			    int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
			            : bounds.outWidth;

			    BitmapFactory.Options opts = new BitmapFactory.Options();
			    opts.inSampleSize = originalSize / Constants.INT_SAMPLE_SIZE;
			    holder.imgPhotoImage.setImageBitmap(BitmapFactory.decodeFile(imgFile.getPath(), opts));   
			}
		} 
		else 
		{
			holder.imgPhotoImage.setImageResource(R.drawable.splash_image);
		}

		return convertView;
	}
	
	private static final class Holder
	{
		ImageView imgPhotoImage;
	}
	
}
