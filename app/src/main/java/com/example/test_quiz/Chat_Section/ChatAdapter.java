package com.example.test_quiz.Chat_Section;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test_quiz.R;

import org.jetbrains.annotations.NotNull;


class ChatAdapter extends ArrayAdapter<ChatMessage> {

    ChatAdapter(Context context, int resource) {
        super(context, resource);
    }

    @NotNull
    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView==null){
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_chat_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.circleImageView = convertView.findViewById(R.id.userChatImage);
            viewHolder.username =  convertView.findViewById(R.id.list_item_username);
            viewHolder.message =  convertView.findViewById(R.id.list_item_message);
            viewHolder.text_time = convertView.findViewById(R.id.text_message_time);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ChatMessage item = getItem(position);
        final long ONE_MEGABYTE = 1024 * 1024;

        /*
        get each user image path of users from firebase storage and set image of user
         */
//        StorageReference firebaseStorage = FirebaseStorage.getInstance().getReferenceFromUrl
//                (Objects.requireNonNull(item).getUser_image_path());
//        firebaseStorage.getBytes(ONE_MEGABYTE)
//                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                    @Override
//                    public void onSuccess(byte[] bytes) {
//                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                        viewHolder.circleImageView.setMinimumHeight(70);
//                        viewHolder.circleImageView.setMinimumWidth(70);
//                        viewHolder.circleImageView.setMaxHeight(70);
//                        viewHolder.circleImageView.setMaxWidth(70);
//                        viewHolder.circleImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                        viewHolder.circleImageView.setImageBitmap(bm);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                viewHolder.circleImageView.setImageResource(R.drawable.speaking);
//            }
//        });
        viewHolder.circleImageView.setImageResource(R.drawable.speaking);
        viewHolder.username.setText(item.getUsername());
        viewHolder.message.setText(item.getMessage());
        viewHolder.text_time.setText(item.getComment_time());

        return convertView;
    }

    private static class ViewHolder{
        TextView username;
        TextView message;
        ImageView circleImageView;
        TextView text_time;
    }
}
