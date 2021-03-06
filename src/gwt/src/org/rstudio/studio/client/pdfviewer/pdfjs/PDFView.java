/*
 * PDFView.java
 *
 * Copyright (C) 2009-12 by RStudio, Inc.
 *
 * This program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
 *
 */
package org.rstudio.studio.client.pdfviewer.pdfjs;


import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import org.rstudio.studio.client.pdfviewer.pdfjs.events.PDFLoadEvent;
import org.rstudio.studio.client.pdfviewer.pdfjs.events.PageChangeEvent;
import org.rstudio.studio.client.pdfviewer.pdfjs.events.ScaleChangeEvent;

public class PDFView extends JavaScriptObject
{
   protected PDFView()
   {
   }

   public static native void nextPage() /*-{
      $wnd.PDFView.page++;
   }-*/;

   public static native void previousPage() /*-{
      $wnd.PDFView.page-- ;
   }-*/;
   
   public static native int currentPage() /*-{
      return $wnd.PDFView.page;
   }-*/;

   public static native int pageCount() /*-{
      return $wnd.PDFView.pages.length;
   }-*/;

   public static native void goToPage(int page) /*-{
      $wnd.PDFView.page = page;
   }-*/;

   public static native double currentScale() /*-{
      return $wnd.PDFView.currentScale;
   }-*/;

   public static native void zoomIn() /*-{
      $wnd.PDFView.zoomIn() ;
   }-*/;
   
   public static native void zoomOut() /*-{
      $wnd.PDFView.zoomOut() ;
   }-*/;

   public native static void parseScale(String value) /*-{
      $wnd.PDFView.parseScale(value);
   }-*/;

   public static HandlerRegistration addPageChangeHandler(PageChangeEvent.Handler handler)
   {
      return handlers_.addHandler(PageChangeEvent.TYPE, handler);
   }

   public static HandlerRegistration addScaleChangeHandler(ScaleChangeEvent.Handler handler)
   {
      return handlers_.addHandler(ScaleChangeEvent.TYPE, handler);
   }

   public static HandlerRegistration addPDFLoadHandler(PDFLoadEvent.Handler handler)
   {
      return handlers_.addHandler(PDFLoadEvent.TYPE, handler);
   }

   public static native void toggleSidebar() /*-{
      $wnd.PDFView.toggleSidebar();
   }-*/;

   public native static void initializeEvents() /*-{

      var _setInitialView = $wnd.PDFView.setInitialView;
      $wnd.PDFView.setInitialView = $entry(function(storedHash, scale) {
         _setInitialView.call($wnd.PDFView, storedHash, scale);
         @org.rstudio.studio.client.pdfviewer.pdfjs.PDFView::firePDFLoadEvent()();
      });

      $wnd.addEventListener(
            "pagechange",
            $entry(function(evt) {
               @org.rstudio.studio.client.pdfviewer.pdfjs.PDFView::firePageChangeEvent()();
            }),
            true);

      $wnd.addEventListener(
            "scalechange",
            $entry(function(evt) {
               @org.rstudio.studio.client.pdfviewer.pdfjs.PDFView::fireScaleChangeEvent()();
            }),
            true);
   }-*/;

   private static void firePageChangeEvent()
   {
      handlers_.fireEvent(new PageChangeEvent());
   }

   private static void fireScaleChangeEvent()
   {
      handlers_.fireEvent(new ScaleChangeEvent());
   }

   private static void firePDFLoadEvent()
   {
      handlers_.fireEvent(new PDFLoadEvent());
   }

   public static void setLoadingVisible(boolean visible)
   {
      Element el = Document.get().getElementById("loading");
      if (visible)
         el.removeAttribute("hidden");
      else
         el.setAttribute("hidden", "hidden");
   }

   public static native void navigateTo(JavaScriptObject dest) /*-{
      if (dest == null)
         return;

      $wnd.PDFView.parseScale(dest.scale);
      $wnd.scrollTo(dest.x, dest.y);
   }-*/;

   public static native JavaScriptObject getNavigateDest() /*-{
      if ($wnd.PDFView.pages.length == 0)
         return null;
      return {
         scale: $wnd.PDFView.currentScaleValue,
         x: $wnd.scrollX,
         y: $wnd.scrollY
      };
   }-*/;

   public static native void scrollToTop() /*-{
      $wnd.scrollTo($wnd.scrollX, 32); // a little padding on top
   }-*/;

   private static final HandlerManager handlers_ =
         new HandlerManager(PDFView.class);
}
