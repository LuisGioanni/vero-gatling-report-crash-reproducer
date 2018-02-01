package com.ojingolabs.vero.test.subroutines

class Post(postType: String) extends Endpoint("social:share:".concat(postType)) {}
