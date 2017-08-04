**5.0.0-alpha1**
 - vector drawables support
 - Java8 support
 - fixes

Added methods:
 
 - SearchView.addDivider(RecyclerView.ItemDecoration itemDecoration)
 - SearchView.removeDivider(RecyclerView.ItemDecoration itemDecoration)
 - SearchView.setNavigationIconListener(View.OnClickListener listener)
 

**4.0.0**  
News:
 - fixed many issues and bugs.
 - added SearchBehavior for work in CoordinatorLayout.
 - SearchView.setArrowOnly
 - SearchView.setInputType
 - SearchView.getInputType
 - SearchView.setImeOptions
 - SearchView.getImeOptions
 
Changes:
 - SearchView.setTextInput has been changed to SearchView.setTextOnly.
 - method SearchView.setOnOpenCloseListener() now returns boolean values.
 - method setNavigationIconArrowHamburger() was deprecated. Do not use it anymore.