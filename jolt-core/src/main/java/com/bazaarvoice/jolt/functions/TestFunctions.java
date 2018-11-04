package com.bazaarvoice.jolt.functions;

public final class TestFunctions {

   @Function( "isTrue" )
   public boolean isTrueMember( @DefaultParam boolean b )
   {
      return Boolean.TRUE.equals( b );
   }

   @Function( "intAdd" )
   public static int intAdd( int x, int y ) {
      return x + y;
   }

   @Function( "trim" )
   public static String trim( @DefaultParam String str ) {
      return str.trim();
   }
}
