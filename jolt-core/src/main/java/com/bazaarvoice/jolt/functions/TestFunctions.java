package com.bazaarvoice.jolt.functions;

public final class TestFunctions {

   @Function( "isTrueStatic" )
   public static boolean isTrueStatic( @DefaultParam boolean b )
   {
      return Boolean.TRUE.equals( b );
   }

   @Function( "isTrueMember" )
   public boolean isTrueMember( @DefaultParam boolean b )
   {
      return Boolean.TRUE.equals( b );
   }

   @Function( "intSum" )
   public static int intSum( int x, int y ) {
      return x + y;
   }

   @Function( "trim" )
   public static String trim( String str ) {
      return str.trim();
   }
}
