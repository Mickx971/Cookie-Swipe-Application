/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.search.SearchTerm;

/**
 *
 * @author mickx
 */
public class CustomMessage extends Message {
    
    private Message m;
    private MailAccount ma;
    

    public CustomMessage(Message m, MailAccount ma) {
        this.m = m;
        this.ma = ma;
    }
    
    public Message getMessage() {
        return m;
    }
    
    public MailAccount getMailAccount() {
        return ma;
    }
    
    public Address[] getFrom() throws MessagingException {
        return m.getFrom();
    }

    public void setFrom() throws MessagingException {
        m.setFrom();
    }

    public void setFrom(Address adrs) throws MessagingException {
        m.setFrom(adrs);
    }

    public void addFrom(Address[] adrss) throws MessagingException {
        m.addFrom(adrss);
    }

    public Address[] getRecipients(RecipientType rt) throws MessagingException {
        return m.getRecipients(rt);
    }

    public Address[] getAllRecipients() throws MessagingException {
        return m.getAllRecipients();
    }

    public void setRecipients(RecipientType rt, Address[] adrss) throws MessagingException {
        m.setRecipients(rt, adrss);
    }

    public void setRecipient(RecipientType rt, Address adrs) throws MessagingException {
        m.setRecipient(rt, adrs);
    }

    public void addRecipients(RecipientType rt, Address[] adrss) throws MessagingException {
        m.addRecipients(rt, adrss);
    }

    public void addRecipient(RecipientType rt, Address adrs) throws MessagingException {
       m.addRecipient(rt, adrs);
    }

    public Address[] getReplyTo() throws MessagingException {
        return m.getReplyTo();
    }

    public void setReplyTo(Address[] adrss) throws MessagingException {
        m.setReplyTo(adrss);
    }

    public String getSubject() throws MessagingException {
        return m.getSubject();
    }

    public void setSubject(String string) throws MessagingException {
        m.setSubject(string);
    }

    public Date getSentDate() throws MessagingException {
        return m.getSentDate();
    }

    public void setSentDate(Date date) throws MessagingException {
        m.setSentDate(date);
    }

    public Date getReceivedDate() throws MessagingException {
        return m.getReceivedDate();
    }

    public Flags getFlags() throws MessagingException {
        return m.getFlags();
    }

    public boolean isSet(Flags.Flag flag) throws MessagingException {
        return m.isSet(flag);
    }

    public void setFlags(Flags flags, boolean bln) throws MessagingException {
        m.setFlags(flags, bln);
    }

    public void setFlag(Flags.Flag flag, boolean bln) throws MessagingException {
        m.setFlag(flag, bln);
    }

    public int getMessageNumber() {
        return m.getMessageNumber();
    }

    public Folder getFolder() {
        return m.getFolder();
    }

    public boolean isExpunged() {
        return m.isExpunged();
    }

    public Message reply(boolean bln) throws MessagingException {
        return m.reply(bln);
    }

    public void saveChanges() throws MessagingException {
        m.saveChanges();
    }

    public boolean match(SearchTerm st) throws MessagingException {
        return m.match(st);
    }

    @Override
    public int getSize() throws MessagingException {
        return m.getSize();
    }

    @Override
    public int getLineCount() throws MessagingException {
        return m.getLineCount();
    }

    @Override
    public String getContentType() throws MessagingException {
        return m.getContentType();
    }

    @Override
    public boolean isMimeType(String string) throws MessagingException {
        return m.isMimeType(string);
    }

    @Override
    public String getDisposition() throws MessagingException {
        return m.getDisposition();
    }

    @Override
    public void setDisposition(String string) throws MessagingException {
        m.setDisposition(string);
    }

    @Override
    public String getDescription() throws MessagingException {
        return m.getDescription();
    }

    @Override
    public void setDescription(String string) throws MessagingException {
        m.setDescription(string);
    }

    @Override
    public String getFileName() throws MessagingException {
        return m.getFileName();
    }

    @Override
    public void setFileName(String string) throws MessagingException {
        m.setFileName(string);
    }

    @Override
    public InputStream getInputStream() throws IOException, MessagingException {
        return m.getInputStream();
    }

    @Override
    public DataHandler getDataHandler() throws MessagingException {
        return m.getDataHandler();
    }

    @Override
    public Object getContent() throws IOException, MessagingException {
        return m.getContent();
    }

    @Override
    public void setDataHandler(DataHandler dh) throws MessagingException {
        m.setDataHandler(dh);
    }

    @Override
    public void setContent(Object o, String string) throws MessagingException {
        m.setContent(o, string);
    }

    @Override
    public void setText(String string) throws MessagingException {
        m.setText(string);
    }

    @Override
    public void setContent(Multipart mltprt) throws MessagingException {
        m.setContent(mltprt);
    }

    @Override
    public void writeTo(OutputStream out) throws IOException, MessagingException {
        m.writeTo(out);
    }

    @Override
    public String[] getHeader(String string) throws MessagingException {
        return m.getHeader(string);
    }

    @Override
    public void setHeader(String string, String string1) throws MessagingException {
        m.setHeader(string, string1);;
    }

    @Override
    public void addHeader(String string, String string1) throws MessagingException {
        m.addHeader(string, string1);
    }

    @Override
    public void removeHeader(String string) throws MessagingException {
        m.removeHeader(string);
    }

    @Override
    public Enumeration getAllHeaders() throws MessagingException {
        return m.getAllHeaders();
    }

    @Override
    public Enumeration getMatchingHeaders(String[] strings) throws MessagingException {
        return m.getMatchingHeaders(strings);
    }

    @Override
    public Enumeration getNonMatchingHeaders(String[] strings) throws MessagingException {
        return m.getNonMatchingHeaders(strings);
    }
    
    @Override
    public String toString() {
        return m.toString();
    }
    
    @Override 
    public boolean equals(Object o) {
        return m.equals(o);
    }
    
    @Override 
    public int hashCode() {
        return m.hashCode();
    }
    
}
