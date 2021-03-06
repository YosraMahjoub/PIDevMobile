/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

/**
 *
 * @author HELA
 */
 import com.codename1.components.ScaleImageButton;
import com.codename1.components.SpanButton;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;

import java.util.List;

import static com.codename1.ui.CN.getCurrentForm;
import static com.codename1.ui.ComponentSelector.select;


public abstract class Demo {


/** 
 * This is the base class for all the demos.
 */

    private String id;
    private Image demoImage;
    private Form parentForm;
    private String sourceCode;
    
    protected void init(String id, Image demoImage, Form parentForm, String sourceCode){
        this.id = id;
        this.demoImage = demoImage;
        this.parentForm = parentForm;
        this.sourceCode = sourceCode;
    }
    
    protected String getSourceCode(){
        return sourceCode;
    }

    protected String getDemoId(){
        return id;
    }
   
    protected Image getDemoImage(){
        return demoImage;
    }
    
    protected Form getParentForm(){
        return parentForm;
    }

    /**
     * Build the content of the demo that derives this class.
     *
     * @return container that contain the demo content.
     */
    abstract public Container createContentPane();

    protected void showDemo(String title, Component content){
            Form demoForm = new Form(title, new BorderLayout());
            content.setUIID("ComponentDemoContainer");
            Toolbar toolbar = demoForm.getToolbar();
            toolbar.setUIID("DemoToolbar");
            toolbar.getTitleComponent().setUIID("ComponentDemoTitle");

            Form lastForm = getCurrentForm();
            Command backCommand = Command.create("", FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, UIManager.getInstance().getComponentStyle("DemoTitleCommand")),
                    e-> lastForm.showBack());

            toolbar.setBackCommand(backCommand);
            demoForm.add(BorderLayout.CENTER, content);
            demoForm.setFormBottomPaddingEditingMode(true);
            demoForm.show();
    }
    
    public static void adjustToTablet(Container cnt){
        // Create anonymous class and override the calcPreferredSize() function to fit exactly half of the scree.
        Container leftSide = new Container(new BoxLayout(BoxLayout.Y_AXIS)){
            @Override
            protected Dimension calcPreferredSize() {
                Dimension dim = super.calcPreferredSize();
                dim.setWidth(Display.getInstance().getDisplayWidth() / 2);
                return dim;
            }
        };
        
        Container rightSide = new Container(new BoxLayout(BoxLayout.Y_AXIS)){
            @Override
            protected Dimension calcPreferredSize() {
                Dimension dim = super.calcPreferredSize();
                dim.setWidth(Display.getInstance().getDisplayWidth() / 2);
                return dim;
            }
        };

        int i = 0;
        for(Component currComponent : cnt.getChildrenAsList(true)){
            cnt.removeComponent(currComponent);
            if(i++ % 2 == 0){
                leftSide.add(currComponent);
            }else{
                rightSide.add(currComponent);
            }
        }
        cnt.setLayout(new BoxLayout(BoxLayout.X_AXIS));
        cnt.addAll(leftSide, rightSide);
    }

    public Component createComponent(Image image, String header, String firstLine, String body, ActionListener<ActionEvent> listener){
        return new AccordionComponent(image, header, firstLine, body, listener);
    }

    public Component createComponent(Image image, String header, String firstLine, ActionListener<ActionEvent> listener){
        ScaleImageButton contentImage = new ScaleImageButton(image){
            @Override
            protected Dimension calcPreferredSize() {

                Dimension preferredSize =  super.calcPreferredSize();
                preferredSize.setHeight(Display.getInstance().getDisplayHeight() / 10);
                return preferredSize;
            }
        };
        contentImage.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED);

        contentImage.addActionListener(listener);
        contentImage.setUIID("DemoContentImage");
        Label contentHeader = new Button(header, "DemoContentHeader");
        Label contentFirstLine = new Button(firstLine, "DemoContentBody");

        Container demoContent = BoxLayout.encloseY(contentImage, contentHeader, contentFirstLine);
        contentImage.setWidth(demoContent.getWidth() - demoContent.getAllStyles().getPadding(Component.LEFT) - demoContent.getAllStyles().getPadding(Component.RIGHT) - contentImage.getAllStyles().getPadding(Component.LEFT) - contentImage.getAllStyles().getPadding(Component.RIGHT));
        demoContent.setLeadComponent(contentImage);
        demoContent.setUIID("DemoContentRegular");
        return demoContent;
    }

    private static class AccordionComponent extends Container{
        private boolean isOpen = false;
        private final Button firstLine;
        private final SpanButton body;
        private final Image openedIcon;
        private final Image closedIcon;
        private final Button openClose;

        /**
         * Demo component that have more then one line of description.
         *
         * @param image the image of the component.
         * @param header the header of the component.
         * @param firstLine first line of description.
         * @param body the rest of the description.
         * @param listener add ActionListener to the image of the component.
         */
        private AccordionComponent(Image image, String header, String firstLine, String body, ActionListener<ActionEvent> listener) {
            super(new BorderLayout());
            this.firstLine = new Button(firstLine + " " + body, "DemoContentBody");
            this.body = new SpanButton(firstLine + " " + body, "DemoContentBody");
            this.body.setUIID("DemoContentBodyButton");

            this.firstLine.addActionListener(listener);
            this.body.addActionListener(listener);
            Container contentContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));

            setUIID("DemoContentAccordion");
            ScaleImageButton contentImage = new ScaleImageButton(image){
                @Override
                protected Dimension calcPreferredSize() {

                    Dimension preferredSize =  super.calcPreferredSize();
                    preferredSize.setHeight(Display.getInstance().getDisplayHeight() / 10);
                    return preferredSize;
                }
            };

            contentImage.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED);
            contentImage.addActionListener(listener);
            contentImage.setUIID("DemoContentImage");
            Button contentHeader = new Button(header, "DemoContentHeader");
            contentHeader.addActionListener(listener);

            Style buttonStyle = UIManager.getInstance().getComponentStyle("AccordionButton");
            openedIcon = FontImage.createMaterial(FontImage.MATERIAL_KEYBOARD_ARROW_UP, buttonStyle);
            closedIcon = FontImage.createMaterial(FontImage.MATERIAL_KEYBOARD_ARROW_DOWN, buttonStyle);
            openClose = new Button("", closedIcon, "AccordionButton");
            openClose.addActionListener(e->{
                if(isOpen){
                    close(true);
                }else{
                    open();
                }
            });

            contentContainer.addAll(contentHeader, this.firstLine, this.body);
            this.body.setHidden(true);
            add(BorderLayout.NORTH, contentImage);
            add(BorderLayout.CENTER, contentContainer);
            add(BorderLayout.EAST, openClose);
        }

        public void open(){
            // Select all AccordionComponent objects to close them when we open another one.
            List<Component> accordionList = select("DemoContentAccordion").asList();
            for(Component currComponent: accordionList){
                ((AccordionComponent)currComponent).close(false);
            }

            if (!isOpen){
                isOpen = true;
                openClose.setIcon(openedIcon);
                this.body.setHidden(false);
                this.firstLine.setHidden(true);
                this.getParent().animateLayout(500);
            }
        }

        public void close(boolean shouldAnimate){
            if (isOpen){
                isOpen = false;
                openClose.setIcon(closedIcon);
                this.body.setHidden(true);
                this.firstLine.setHidden(false);
                if (shouldAnimate){
                    this.getParent().animateLayout(500);
                }
            }
        }
    }
}