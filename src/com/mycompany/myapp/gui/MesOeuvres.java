/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

import com.codename1.components.ImageViewer;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.notifications.LocalNotification;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.BOTTOM;
import static com.codename1.ui.Component.CENTER;
import static com.codename1.ui.Component.LEFT;
import static com.codename1.ui.Component.RIGHT;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextArea;
import com.codename1.ui.Toolbar;
import com.codename1.ui.URLImage;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import com.mycompany.myapp.entities.Oeuvre;
import com.mycompany.myapp.services.OeuvrageService;
import com.mycompany.myapp.services.UserService;

/**
 *
 * @author pc
 */
public class MesOeuvres extends BaseForm {
    Label comments;
    Form f;
int i = UserService.getCurrentUser().getUser_id();
    Container cn1;
    ImageViewer iv;
    URLImage img;
    Label lblnomeve;
    Label nomeve;
    Label promo;
     EncodedImage enc;
     String urlimg = "http://localhost/PIDevWEB-main/public/PI/IMG/";
    public MesOeuvres(Form previous, Resources res) {  
super("Liste des favoris", BoxLayout.y());
Toolbar tb = new Toolbar(true);  
        setToolbar(tb);
       // getTitleArea().setUIID("Container");
        //setTitle("Liste des oeuvres");
        getContentPane().setScrollVisible(false);
        
    super.addSideMenu(res);
      tb.addSearchCommand(e -> {});
        
        Tabs swipe = new Tabs();

        Label spacer1 = new Label();
        Label spacer2 = new Label();
       addTab(swipe, res.getImage("news-item.jpg"), spacer1, " Mes oeuvres");
        
                
        swipe.setUIID("Container");
        swipe.getContentPane().setUIID("Container");
        swipe.hideTabs();
        
        ButtonGroup bg = new ButtonGroup();
        int size = Display.getInstance().convertToPixels(1);
      Image unselectedWalkthru = Image.createImage(size, size, 0);
        Graphics g = unselectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAlpha(100);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        Image selectedWalkthru = Image.createImage(size, size, 0);
        g = selectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        RadioButton[] rbs = new RadioButton[swipe.getTabCount()];
        FlowLayout flow = new FlowLayout(CENTER);
        flow.setValign(BOTTOM);
        Container radioContainer = new Container(flow);
        for(int iter = 0 ; iter < rbs.length ; iter++) {
            rbs[iter] = RadioButton.createToggle(unselectedWalkthru, bg);
            rbs[iter].setPressedIcon(selectedWalkthru);
            rbs[iter].setUIID("Label");
            radioContainer.add(rbs[iter]);
        }
                
       // rbs[0].setSelected(true);
        swipe.addSelectionListener((i, ii) -> {
            if(!rbs[ii].isSelected()) {
                rbs[ii].setSelected(true);
            }
        });
      Component.setSameSize(radioContainer, spacer1, spacer2);
        
   add(LayeredLayout.encloseIn(swipe, radioContainer));
      
        OeuvrageService ME = new OeuvrageService();
        for (Oeuvre eee : ME.getmesOeuvres(i)) {
            System.out.println(ME.getmesOeuvres(i).size());
               enc = EncodedImage.createFromImage(Image.createImage(this.getWidth()/3, this.getWidth()/3),false);  
                    URLImage  img = URLImage.createToStorage(enc, eee.getImg(), urlimg+eee.getImg());
                    if (eee.getQuantite()==0){
//            iv.setImage(img.scaled(400, 400));
                addButton(previous, res , eee, img, eee.getNom(),false, eee.getPrix(),"hors stock",eee.getDescription());
            }
            else {
                  addButton(previous, res , eee ,img, eee.getNom(),false, eee.getPrix(),String.valueOf((int)eee.getQuantite())+ " Oeuvres",eee.getDescription());
            }
      if (eee.getIsvalid()==2){
          System.out.println(eee.getIsvalid());
           LocalNotification ln = new LocalNotification();
            ln.setId("LnMessage");
            ln.setAlertTitle("Attention");
            ln.setAlertBody("vous avez des oeuvres invalid !");
            Display.getInstance().scheduleLocalNotification(ln,System.currentTimeMillis(), LocalNotification.REPEAT_MINUTE);
          System.out.println(System.currentTimeMillis());
          System.out.println(System.currentTimeMillis()+1);
          System.out.println(System.currentTimeMillis());
      }
      //  addButton(res.getImage("news-item-2.jpg"), "Fusce ornare cursus masspretium tortor integer placera.", true, 15, 21);
        //addButton(res.getImage("news-item-3.jpg"), "Maecenas eu risus blanscelerisque massa non amcorpe.", false, 36, 15);
       // addButton(res.getImage("news-item-4.jpg"), "Pellentesque non lorem diam. Proin at ex sollicia.", false, 11, 9);
    }  }
  
    
    private void updateArrowPosition(Button b, Label arrow) {
        arrow.getUnselectedStyle().setMargin(LEFT, b.getX() + b.getWidth() / 2 - arrow.getWidth() / 2);
        arrow.getParent().repaint();
        
        
    }
    
private void addTab(Tabs swipe, Image img, Label spacer, String text) {
        int size = Math.min(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
        if(img.getHeight() < size) {
            img = img.scaledHeight(size);
        }
        if(img.getHeight() > Display.getInstance().getDisplayHeight() / 2) {
            img = img.scaledHeight(Display.getInstance().getDisplayHeight() / 2);
        }
        ScaleImageLabel image = new ScaleImageLabel(img);
        image.setUIID("Container");
        image.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        Label overlay = new Label("", "ImageOverlay");
         SpanLabel sl = new SpanLabel(text, "Title");
         
        Container page1 = 
            LayeredLayout.encloseIn(
                image,
              sl,
                BorderLayout.south(
                    BoxLayout.encloseY(
                            overlay,
                           //   FlowLayout.encloseIn(null),
                            spacer
                        )
                )
            );

        swipe.addTab("", page1);
    }
    
 private void addButton(Form previous, Resources res ,Oeuvre o ,URLImage img, String nom , boolean liked, float prix,String quantit??, String description) {
       int height = Display.getInstance().convertToPixels(20f);
       int width = Display.getInstance().convertToPixels(14f);
       Button image = new Button(img.fill(width, height));
       image.setUIID("Label");
       Container cnt = BorderLayout.west(image);
       cnt.setLeadComponent(image);
       TextArea ta = new TextArea(nom);
       ta.setUIID("NewsTopLine1");
       ta.setEditable(false);
       Label desc = new Label(description, "Label");
       Label likes = new Label(prix + " DT ", "LabelPrix");
       likes.setTextPosition(RIGHT);
//       if(!liked) {
//           FontImage.setMaterialIcon(likes, FontImage.MATERIAL_FAVORITE);
//       } else {
//           Style s = new Style(likes.getUnselectedStyle());
//           s.setFgColor(0xff2d55);
//           FontImage heartImage = FontImage.createMaterial(FontImage.MATERIAL_FAVORITE, s);
//           likes.setIcon(heartImage);
//       }

if(o.getIsvalid()==0){
     comments = new Label("en attente" , "Label");
}
else if(o.getIsvalid()==2){
     comments = new Label("n'est pas valid??" , "Label");
}
else if(o.getIsvalid()==1){


        comments = new Label(quantit?? , "Label");
//       FontImage.setMaterialIcon(likes, FontImage.MATERIAL_CHAT);
 }
//       FontImage.setMaterialIcon(likes, FontImage.MATERIAL_CHAT);
      
       //  .setMaterialIcon(likes, FontImage.MATERIAL_CHAT);
       cnt.add(BorderLayout.EAST, 
               BoxLayout.encloseY(
                       ta,
                       desc,
                       BoxLayout.encloseX(likes, comments)
               ));
     Container cnt1 = new Container(BoxLayout.x()); 
     Container cnt2 = new Container(BoxLayout.xRight()); 
       cnt1.add(cnt);           
     
        cnt1.add(cnt2); 
    
       add(cnt1);
       
       image.addActionListener( e -> 
               new DetailOeuvre(previous, res, o).show()
       );
//       raef
//       btnListTasks.addActionListener
   }
    
    private void bindButtonSelection1(Form previous,Resources res, Button b, Label arrow) {
       b.addActionListener(e -> {
            if(b.isSelected()) {
                updateArrowPosition(b, arrow);
            }
        });
    }
    
    private void bindButtonSelection2(Form previous,Resources res, Button b, Label arrow) {
       b.addActionListener(e -> {
            if(b.isSelected()) {
                updateArrowPosition(b, arrow);
                new ListeFavoris(previous, res).show();
            }
        });
    }
    
    private void bindButtonSelection3(Form previous,Resources res, Button b, Label arrow) {
       b.addActionListener(e -> {
            if(b.isSelected()) {
                updateArrowPosition(b, arrow);
                
            }
        });
    }

    
}
