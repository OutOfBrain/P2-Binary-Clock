package org.tcial.p2.binaryClock;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class BinaryClock implements Runnable
{

  private TrayIcon icon;

  public BinaryClock()
  {
    // set systray up
    SystemTray sysTray = SystemTray.getSystemTray();
    this.icon = new TrayIcon(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB));

    // add close capabilities
    PopupMenu popupmenu = new PopupMenu();
    MenuItem closeItem = new MenuItem("close");
    closeItem.addActionListener(new ActionListener()
    {

      @Override
      public void actionPerformed(ActionEvent e)
      {
        // TODO: soft close
        System.exit(0);
      }
    });
    popupmenu.add(closeItem);
    this.icon.setPopupMenu(popupmenu);

    // show tray
    try
    {
      sysTray.add(this.icon);
    }
    catch (AWTException e)
    {
      e.printStackTrace();
    }
  }

  void draw(Graphics g, int value, int[] xcoords, int[] ycoords, Color color, Color defaultColor)
  {
    int bits = 0;
    while (bits < xcoords.length)
    {
      if ((value & 1) == 1)
      {
        // bit set
        g.setColor(color);
      }
      else
      {
        // bit not set
        g.setColor(defaultColor);
      }

      value >>= 1;

      g.fillRect(xcoords[bits], ycoords[bits], 2, 2);
      bits += 1;
    }
  }

  @Override
  public void run()
  {
    // update every second
    while (true)
    {
      try
      {
        Thread.sleep(1000);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }

      // draw stuff
      Image image = this.icon.getImage();
      Graphics g = image.getGraphics();

      Calendar now = new GregorianCalendar();
      int hour = now.get(Calendar.HOUR);
      int minute = now.get(Calendar.MINUTE);
      int second = now.get(Calendar.SECOND);

      // draw hours
      int[] hourx =
      { 13, 10, 7, 4, 1 };
      int[] houry =
      { 1, 1, 1, 1, 1 };
      this.draw(g, hour, hourx, houry, Color.RED, Color.BLACK);

      // draw minutes
      int[] minutex =
      { 13, 10, 7, 13, 10, 7 };
      int[] minutey =
      { 7, 7, 7, 4, 4, 4 };
      this.draw(g, minute, minutex, minutey, Color.YELLOW, Color.BLACK);

      // draw seconds
      int[] secondx =
      { 13, 10, 7, 13, 10, 7 };
      int[] secondy =
      { 13, 13, 13, 10, 10, 10 };
      this.draw(g, second, secondx, secondy, Color.GREEN, Color.BLACK);

      // update icon
      this.icon.setImage(image);
    }
  }

  public static void main(String[] args)
  {
    new Thread(new BinaryClock()).start();
  }
}
