package com.example.collegedocumentsharing;

import com.google.firebase.firestore.DocumentSnapshot;

public interface ClassCardViewInterface {
    void OnClassItemClick(DocumentSnapshot documentSnapshot, int position);

}
