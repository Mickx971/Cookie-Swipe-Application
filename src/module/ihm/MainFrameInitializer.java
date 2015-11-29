/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module.ihm;

import interfaces.AbstractIHMAction;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.HashMap;

import javax.mail.Message;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import model.Mail;
import model.MailAccount;
import model.MailComparator;
import model.User;
import network.mail.FolderManager;
import view.component.CookieSwipeButton;
import view.component.CookieSwipeFrame;
import view.component.CookieSwipeList;
import view.component.CookieSwipeTree;
import controller.ActionName;
import controller.Dispatcher;
import cookie.swipe.application.CookieSwipeApplication;
import cookie.swipe.application.utils.ObservableLinkedHashSetPriorityQueue;
import model.CustomMessage;
import view.component.CookieSwipeButtonSprite;

/**
 *
 * @author Lucas
 */
public class MainFrameInitializer extends AbstractIHMAction {

    public static final String jListMailModels = "jListMailModels";

    private CookieSwipeFrame csFrame;

    public MainFrameInitializer(CookieSwipeFrame csFrame) {
        super(csFrame);
        this.csFrame = csFrame;
    }

    public MainFrameInitializer() {
        this(CookieSwipeApplication.getApplication().getMainFrame());
    }

    @Override
    public boolean execute(Object... object) {
        initMailAccount();
        initButton();
        initMailList();
        return true;
    }

    private void initMailList() {
        @SuppressWarnings("unchecked")
        CookieSwipeList<Mail> jListMail = (CookieSwipeList<Mail>) hsJcomponent.get("jListMail");
        jListMail.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent e) {
                setMailButtonsVisible(false);
            }

            @Override
            public void focusGained(FocusEvent e) {
                setSelectedMail(e);
                setMailButtonsVisible(true);
            }
        });
        JScrollPane jScrollPaneMailList = (JScrollPane) hsJcomponent.get("jScrollPaneMailList");
        JScrollBar vBar = jScrollPaneMailList.getVerticalScrollBar();
        vBar.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                CookieSwipeTree myTree = (CookieSwipeTree) hsJcomponent.get("cookieSwipeTreeAccountMail");
                if (myTree.getLastSelectedPathComponent() == MailAccount.ALL) {
                    return;
                }
                int extent = jScrollPaneMailList.getVerticalScrollBar().getModel().getExtent();
                int currentScrollValue = jScrollPaneMailList.getVerticalScrollBar().getValue() + extent;
                int maxScrollValue = jScrollPaneMailList.getVerticalScrollBar().getMaximum();
                if (maxScrollValue != 0 && currentScrollValue == maxScrollValue) {
                    FolderManager folderManager = (FolderManager) CookieSwipeApplication.getApplication().getParam("FolderManager");
                    MailAccount mc = (MailAccount) CookieSwipeApplication.getApplication().getParam("mailAccountSelected");
                    String folderName = (String) CookieSwipeApplication.getApplication().getParam("folderName");
                    DefaultMutableTreeNode lastSelection = (DefaultMutableTreeNode) myTree.getLastSelectedPathComponent();
                    if(lastSelection.getUserObject().equals(mc)) {
                        folderName = mc.getDefaultFolderName();
                    }
                    if (mc != null && folderName != null && !folderName.isEmpty() && mc.isReady()) {
                        folderManager.retrieveNextMailsFor(mc, folderName);
                    }
                }
            }
        });
    }

    public DefaultMutableTreeNode getRootNode() {
        DefaultTreeModel model = getModel();
        return (DefaultMutableTreeNode) model.getRoot();
    }

    public DefaultTreeModel getModel() {
        CookieSwipeTree myTree = (CookieSwipeTree) hsJcomponent.get("cookieSwipeTreeAccountMail");
        return (DefaultTreeModel) myTree.getModel();
    }

    public DefaultMutableTreeNode getNode(String name) {
        DefaultMutableTreeNode rootNode = getRootNode();
        @SuppressWarnings("unchecked")
        Enumeration<DefaultMutableTreeNode> en = rootNode.breadthFirstEnumeration();
        while (en.hasMoreElements()) {
            DefaultMutableTreeNode node = en.nextElement();
            TreeNode[] path = node.getPath();
            if (path[path.length - 1].toString().equals(name)) {
                return node;
            }
        }
        return null;
    }

    public void deleteMailAccountInTree(MailAccount mc) {
        DefaultTreeModel model = getModel();
        DefaultMutableTreeNode rootNode = getRootNode();
        rootNode.remove(getNode(mc.getCSName()));
        model.nodeStructureChanged(rootNode);
    }

    public void addMailAccountInTree(MailAccount mc) {
        DefaultTreeModel model = getModel();
        DefaultMutableTreeNode rootNode = getRootNode();
        rootNode.add(new DefaultMutableTreeNode(mc));
        model.nodeStructureChanged(rootNode);
    }

    public void addFolderInTree(MailAccount mailAccount, String folderName) {
        DefaultMutableTreeNode folder = getNode(mailAccount.getCSName());
        folder.add(new DefaultMutableTreeNode(folderName));
        getModel().nodeStructureChanged(folder);
    }

    private void initModels() {
        HashMap<String, HashMap<String, CustomJListModel>> models = new HashMap<>();
        CookieSwipeApplication.getApplication().setParam(jListMailModels, models);
        HashMap<String, CustomJListModel> allModel = new HashMap<>();
        allModel.put(MailAccount.ALL, new CustomJListModel(new ObservableLinkedHashSetPriorityQueue(new MailComparator())));
        models.put(MailAccount.ALL, allModel);
    }

    @SuppressWarnings("unchecked")
    private void initMailAccount() {
        DefaultMutableTreeNode myRoot = new DefaultMutableTreeNode(MailAccount.ALL);

        MailAccount firstMailAccount = null;
        MutableTreeNode firstFolder = null;

        // Construction des différents noeuds de l'arbre.
        User user = CookieSwipeApplication.getApplication().getUser();
        for (MailAccount mailAccount : user.getListOfMailAccount()) {
            if (firstMailAccount != null) {
                myRoot.add(new DefaultMutableTreeNode(mailAccount));
            } else {
                firstMailAccount = mailAccount;
                firstFolder = new DefaultMutableTreeNode(mailAccount);
                myRoot.add(firstFolder);
            }
        }

        // Creation des models de données pour les listes de mails à afficher
        initModels();

        // Construction de l'arbre.
        CookieSwipeTree myTree = (CookieSwipeTree) hsJcomponent.get("cookieSwipeTreeAccountMail");
        myTree.setModel(new DefaultTreeModel(myRoot));

        if (firstMailAccount != null) {
            Enumeration<DefaultMutableTreeNode> en = myRoot.breadthFirstEnumeration();
            en.nextElement();
            DefaultMutableTreeNode node = en.nextElement();
            myTree.setSelectionPath(new TreePath(node));
            CookieSwipeApplication.getApplication().setParam("mailAccountSelected", firstMailAccount);
        }

        myTree.addMouseListener(new TreeMouseListener(myTree, (JList<CustomMessage>) hsJcomponent.get("jListMail")));
    }

    private void initButton() {
        Dispatcher dispatcher = new Dispatcher();

        CookieSwipeButton button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonUpdateCSAccount");
        button.setActionCommand(ActionName.updateAccount);
        button.addActionListener(dispatcher);

        button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonLogout");
        button.setActionCommand(ActionName.logout);
        button.addActionListener(dispatcher);

        button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonAddMailAccount");
        button.setActionCommand(ActionName.addMailAccount);
        button.addActionListener(dispatcher);

        button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonUpdateMailAccount");
        button.setActionCommand(ActionName.selectMailAccount);
        button.addActionListener(dispatcher);

        button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonDeleteMailAccount");
        button.setActionCommand(ActionName.deleteMailAccount);
        button.addActionListener(dispatcher);

        button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonNewMail");
        button.setActionCommand(ActionName.writeMail);
        button.addActionListener(dispatcher);

        button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonReply");
        button.setActionCommand(ActionName.reply);
        button.addActionListener(dispatcher);

        button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonReplyToAll");
        button.setActionCommand(ActionName.replyAll);
        button.addActionListener(dispatcher);
        
        button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonToBlacklist");
        button.setActionCommand(ActionName.addBlackListSender);
        button.addActionListener(dispatcher);
        
        button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonDeleteMail");
        button.setActionCommand(ActionName.deleteMail);
        button.addActionListener(dispatcher);

        button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonForward");
        button.setActionCommand(ActionName.forward);
        button.addActionListener(dispatcher);

        button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonRefresh");
        button.setActionCommand(ActionName.refresh);
        button.addActionListener(dispatcher);

        button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonBlacklist");
        button.setActionCommand(ActionName.showBlacklistSender);
        button.addActionListener(dispatcher);

        setMailAccountButtonsVisible(false);
        setMailButtonsVisible(false);

        refresh();
    }

    private void setMailAccountButtonsVisible(boolean b) {
        CookieSwipeButton button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonUpdateMailAccount");
        button.setVisible(b);
        button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonDeleteMailAccount");
        button.setVisible(b);

        refresh();
    }

    private void setMailButtonsVisible(boolean b) {
        CookieSwipeButton button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonReply");
        button.setVisible(b);
        button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonDeleteMail");
        button.setVisible(b);
        button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonForward");
        button.setVisible(b);
        button = (CookieSwipeButton) hsJcomponent.get("cookieSwipeButtonReplyToAll");
        button.setVisible(b);

        refresh();
    }

    private void setSelectedMail(FocusEvent e) {
        JList<CustomMessage> jListMail = (JList<CustomMessage>) hsJcomponent.get("jListMail");
        CookieSwipeApplication.getApplication().setParam("selectedMail", jListMail.getSelectedValue());
    }

    private class TreeMouseListener extends MouseAdapter {

        private CookieSwipeTree myTree = null;
        private JList<CustomMessage> jListMail = null;

        public TreeMouseListener(CookieSwipeTree myTree, JList<CustomMessage> jListMail) {
            this.myTree = myTree;
            this.jListMail = jListMail;

            this.jListMail.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    JList theList = (JList) e.getSource();
                    if (e.getClickCount() == 2) { // double click
                        int index = theList.locationToIndex(e.getPoint());
                        if (index >= 0) {
                            CustomMessage message = (CustomMessage) theList.getModel().getElementAt(index);
                            CookieSwipeApplication.getApplication().setParam("selectedMail", message);
                            Dispatcher dispatcher = new Dispatcher();
                            dispatcher.readMailAction();
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        }

        @Override
        public void mousePressed(MouseEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) myTree.getLastSelectedPathComponent();
            if (node == null) {
                return;
            }
            CookieSwipeApplication.getApplication().setParam("folderName", node.toString());
            CustomJListModel model = getModelForSelection(node);
            if (model != null) {
                jListMail.setModel(model);
            }
        }

        @SuppressWarnings("unchecked")
        private CustomJListModel getModelForSelection(DefaultMutableTreeNode node) {
            String keyAccount = null, keyFolder = null;
            if (node != null) {
                Object userObject = node.getUserObject();
                if (userObject instanceof MailAccount) {
                    setMailAccountButtonsVisible(true);
                    CookieSwipeApplication.getApplication().setParam("mailAccountSelected", node.getUserObject());
                    MailAccount ma = (MailAccount) userObject;
                    keyAccount = ma.getCSName();
                    keyFolder = ma.getDefaultFolderName();
                } else {
                    setMailAccountButtonsVisible(false);
                    if (node.getUserObject() instanceof String) {
                        if (node.equals(node.getRoot())) {
                            keyAccount = MailAccount.ALL;
                            keyFolder = MailAccount.ALL;
                        } else {
                            MailAccount mc = (MailAccount) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
                            CookieSwipeApplication.getApplication().setParam("mailAccountSelected", mc);
                            keyAccount = mc.getCSName();
                            keyFolder = (String) node.getUserObject();
                        }
                    }
                }
            }

            if (keyAccount == null || keyFolder == null) {
                return null;
            }

            HashMap<String, HashMap<String, CustomJListModel>> models;
            models = (HashMap<String, HashMap<String, CustomJListModel>>) CookieSwipeApplication.getApplication().getParam(jListMailModels);
            return models.get(keyAccount).get(keyFolder);
        }
    }

    private void refresh() {
        csFrame.validate();
        csFrame.repaint();
        csFrame.revalidate();
    }
}
