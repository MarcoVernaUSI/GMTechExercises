package main_package;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class View extends JPanel
    implements ListSelectionListener {
    private final Selector _selector;
    private final ListManager _candidateList;
    
   
    private final JTextField speakerName;
    private final JButton removeButton;
    private final JButton selectButton;
    private final JButton addButton;
    private final JButton logButton;

    public View(Selector selector) {
        super(new BorderLayout());
        
        // Load the selector                                                
        _selector = selector; 
        
        //Create the candidate List
        _candidateList = new ListManager(this);
        
        // Create panel
        JScrollPane listScrollPane = new JScrollPane(_candidateList.getList());
        
        
        // Create the select speaker button
        selectButton = new JButton("Select random speaker");
        selectButton.addActionListener(new SelectListener());

        // Create the remove speaker button
        removeButton = new JButton("Remove speaker");
        removeButton.addActionListener(new RemoveListener());

        // Create the add speaker button
        addButton = new JButton("Add speaker");
        addButton.addActionListener(new AddListener());

        // Print log button
        logButton = new JButton("print Log");
        logButton.addActionListener(new logListener());

        // Create the extracted speaker box
        speakerName = new JTextField(20);
        speakerName.setEditable(false);

        // Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
            BoxLayout.LINE_AXIS));

        buttonPane.add(removeButton);
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(addButton);
        buttonPane.add(logButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel buttonPane2 = new JPanel();
        buttonPane2.setLayout(new BoxLayout(buttonPane2,
            BoxLayout.LINE_AXIS));

        buttonPane2.add(selectButton);
        buttonPane2.add(speakerName);
        buttonPane2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel description = new JLabel("right click for toggle/untoggle absent status");
        
        add(description,BorderLayout.AFTER_LINE_ENDS);
        add(listScrollPane, BorderLayout.PAGE_START);
        add(buttonPane, BorderLayout.CENTER);
        add(buttonPane2, BorderLayout.PAGE_END);
        
        _candidateList.updateList();
    }

    

    // This method is required by ListSelectionListener.
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            if (_candidateList.getList().getSelectedIndex() == -1) {
                // No selection, disable remove button.
                removeButton.setEnabled(false);
            } else {
                // Selection, enable the remove button.
                removeButton.setEnabled(true);
            }
        }
    }

    // Listeners
    class SelectListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            speakerName.setText(_selector.select());
        }
    }

    class RemoveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // get selected index
            String speaker = _candidateList.getList().getSelectedValue();  
            _selector.remove(speaker);  
            _candidateList.updateList();
        }
    }

    class logListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(_selector.printLog());
        }
    }

    class AddListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new AddFrame(_candidateList);
            addButton.setEnabled(false);
        }
    }
    
    
    
    
    
    
    

    
    public class ListManager {
        private final JList<String> candidateList;
        private final DefaultListModel<String> candidateListModel;
        
        public ListManager (View view) {
                        
        
            candidateListModel = new DefaultListModel<>();

            // Create the list and put it in a scroll pane.
            candidateList = new JList<>(candidateListModel);
            candidateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            candidateList.setSelectedIndex(0);
            candidateList.addListSelectionListener(view);
            candidateList.setVisibleRowCount(10);
            candidateList.setCellRenderer(new MyListCellRenderer());
            
            // add mouse listener for absents
            candidateList.addMouseListener(new AbsentListener());
        
            
        }

        // Consistency between view and model
        public void updateList() {
            candidateListModel.removeAllElements();
            for (String speaker : _selector.getSpeakers()) {
                candidateListModel.addElement(speaker);
            }
            if (candidateListModel.getSize() == 0) {  
                selectButton.setEnabled(false);
            }
            if (candidateListModel.getSize() > 0 & !selectButton.isEnabled()) {  
                selectButton.setEnabled(true);
            }
        }
            
        public JList<String> getList() {
            return candidateList;
        }
        
        class AbsentListener extends MouseAdapter {
            
            @Override
            public void mousePressed(MouseEvent e) {
                if ( SwingUtilities.isRightMouseButton(e) ) {      
                    candidateList.setSelectedIndex(candidateList.locationToIndex(e.getPoint()));

                     JPopupMenu menu = new JPopupMenu();
                     JMenuItem setAbsent = new JMenuItem("toggle absent or not");
                     setAbsent.addActionListener(new ActionListener() {
                         @Override
                        public void actionPerformed(ActionEvent e) {
                             _selector.setAbsent(candidateList.getSelectedValue());
                         }
                     });
                     menu.add(setAbsent);
                     menu.show(candidateList, e.getPoint().x, e.getPoint().y);            
                 }
             }
        }

    }
    
    class MyListCellRenderer extends JLabel implements ListCellRenderer {
        public MyListCellRenderer() {
            setOpaque(true);
        }
        @Override
        public Component getListCellRendererComponent(JList paramlist, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.toString());
            if (_selector.checkAbsent(value.toString())) {
                setBackground(Color.RED);
            } else
                setBackground(Color.white);
            if (isSelected) {
                setBackground(getBackground().darker());
           }
            return this;
        }
    }
    

    
    
    
    
    
    

    // Add Frame
    class AddFrame {
        private final JFrame f = new JFrame("Add");
        private final JButton innerAddButton;
        private final JTextField fnameField;
        private final JTextField surnameField;
        private final ListManager _candidatesList;

        class innerAddListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get input in boxes
                String fname = fnameField.getText();
                String surname = surnameField.getText();
                if ((fname.trim().length() > 0) & (surname.trim().length() > 0)) {
                    _selector.add(fname, surname);
                    _candidatesList.updateList();
                }
                addButton.setEnabled(true);
                f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                f.dispose();
            }
        }

        class closeListener extends WindowAdapter {
            @Override
            public void windowClosing(WindowEvent e) {
                addButton.setEnabled(true);
            }
        }

        public AddFrame(ListManager candidatesList) {
            f.setTitle("Add candidate");
            f.setBounds(20, 20, 300, 150);
            f.setResizable(false);
            f.setVisible(true);
            f.addWindowListener(new closeListener());
            _candidatesList = candidatesList;

            // Create the add speaker button
            this.innerAddButton = new JButton("Add speaker");
            innerAddButton.addActionListener(new innerAddListener());

            this.fnameField = new JTextField(20);
            this.surnameField = new JTextField(20);

            // Create a panel that uses BoxLayout.
            JPanel innerPane = new JPanel();
            innerPane.setLayout(new BoxLayout(innerPane,
                BoxLayout.Y_AXIS));

            innerPane.add(new JLabel("Name:"));
            innerPane.add(this.fnameField);
            innerPane.add(new JLabel("Surname:"));
            innerPane.add(this.surnameField);
            innerPane.add(this.innerAddButton);
            innerPane.add(Box.createHorizontalStrut(5));
            innerPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            f.getContentPane().add(innerPane);
        }

    }

}
    
    
